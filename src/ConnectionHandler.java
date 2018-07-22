import java.io.*;

public class ConnectionHandler implements Runnable {
    private User user;

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

        boolean isRunning = true;
        while (isRunning) {
            String line = buffer.readLine();

            if (line.startsWith("@quit")) {
                isRunning = false;
                this.user.socket.close();
                TCPServer.removeUser(this.user);
            } else if (line.startsWith("@list")) {
                listUsers();
            } else if (line.startsWith("@nickname")) {
                setNickname(line);
            } else if (line.startsWith("@dm")) {
                directMessage(line);
            } else {
                TCPServer.broadcast(this.user.toString() + ": " + line);
            }
        }
    }

    public void listUsers() {
        String userList = "";
        for (User user : TCPServer.connections) {
            userList += user.nickname + "\n";
        }
        TCPServer.message(this.user.nickname, userList);
    }

    public void setNickname(String line) {
        //initial an array of strings, fill it with split line on spaces, select index one for nickname
        //set using settingnickname from the User class.
        String[] lineContent;
        lineContent = line.split(" ");
        String newUserName = lineContent[1];
        this.user.settingNickname(newUserName);
    }

    public void directMessage(String line) {
        //

        String[] arrayOfLine = line.split(" ");
        String recievingUser = arrayOfLine[1];
        Integer messageLocation = arrayOfLine[0].length() + 1 + arrayOfLine[1].length();
        String messageConstruction = this.user.nickname + " DMs: " + line.substring(messageLocation);

        TCPServer.message(recievingUser, messageConstruction);
        TCPServer.message(this.user.nickname, messageConstruction);
    }
}