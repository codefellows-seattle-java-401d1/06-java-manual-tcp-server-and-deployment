import java.io.*;

public class ConnectionHandler implements Runnable {
    public User user;

    public ConnectionHandler(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        try {
            handleMessage();
        } catch (IOException e) {
        }
    }

    public void handleMessage() throws IOException {
        InputStream inputStream = this.user.socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(reader);

        OutputStream outputStream = this.user.socket.getOutputStream();
        DataOutputStream backToClient = new DataOutputStream(outputStream);
        String[] lineWords;

        boolean isRunning = true;
        while (isRunning) {
            String line = buffer.readLine();
            String response = line.toUpperCase() + "\n";

            if (line.startsWith("@quit")) {
                isRunning = false;
                this.user.socket.close();
            } else if (line.startsWith("@list")) {
                response = listUsers(line);

//The below for loop iterates through the list of users in the connections
//and uses that information of individuals to display after the command is issue.

                for (int i = 0; i < TCPServer.connections.size(); i++) {
                    System.out.println(TCPServer.connections.get(i));
                    TCPServer.broadcast(TCPServer.connections.get(i).toString() + "\n");
                }

                //@Nickname uses the split function to split at the space between the first line indexed at 1

            } else if (line.startsWith("@nickname")) {
                lineWords = line.split(" ");
                String myNewName = lineWords[1];
                this.user.changeNickname(myNewName);


            } else if (line.startsWith("@dm")) {
                directMessage(line, response);
            } else {
                //run standard chat
                TCPServer.broadcast(this.user.toString() + ": " + line);
            }
        }
    }
    // This is to create direct messages between users.
    private void directMessage(String line, String response) {
        String[] cells = line.split(" ");
        String recipient = cells[1];
        Integer messageIndex = cells[0].length() + 1 + cells[1].length();
        String message = this.user.nickname + " says:" + line.substring(messageIndex);

        // Send dm to both users
        TCPServer.message(recipient, message);
        TCPServer.message(this.user.nickname, message);


        TCPServer.broadcast(this.user.toString() + ": " + response);
    }


    public String listUsers(String line) {
        String response = "";

        for (User user : TCPServer.connections) {
            response += user.toString() + "\n";
        }
        return response;
    }
}