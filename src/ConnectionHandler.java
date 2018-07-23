import java.io.*;
import java.net.Socket;
import java.util.Arrays;

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
            String response = line + "\n";

            if (line.startsWith("@quit")) {
                isRunning = false;
                this.user.socket.close();
            } else if (line.startsWith("@list")) {
                response = listUsers(line);
                TCPServer.broadcast(this.user.toString() + ": " + response);
                System.out.println(response);
            } else if (line.startsWith("@nickname")) {
                String newNickname = setNickname();
                response = "You updated your nickname to " + newNickname;
                TCPServer.broadcast(this.user.toString() + ": " + response);
                System.out.println(response);
            } else if (line.startsWith("@dm")) {
                directMessage(line);
                System.out.println(line);
            }
            else {
                TCPServer.broadcast(this.user.toString() + ": " + response);
            }
//
//            TCPServer.broadcast(this.user.toString() + ": " + response);
        }
    }

    public String listUsers(String line) {
        String response = "\n";
        for (User user : TCPServer.connections) {
            response += "@" + user.toString() + "\n";
        }
        return response;
    }

    public String setNickname() throws IOException {
        // Get current nickname
        InputStream inputStream = this.user.socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(reader);

        String newNickname = "";

        boolean isRunning = true;
        while (isRunning) {
            newNickname = buffer.readLine();
            isRunning = false;
        }

        this.user.nickname = newNickname;
        return newNickname;
    }

    // @dm otherUserName my message blah blah blah
    // [@dm, otherUserName, my, message, blah, blah, blah]
    private void directMessage(String line) {
        String[] cells = line.split(" ");
        System.out.println("cells" + Arrays.toString(cells));
        String user = cells[1];
        System.out.println("cells[1]" + cells[1]);

        int secondSpaceIndex = line.indexOf(" ", 4);
        String message = line.substring(secondSpaceIndex);
        message = this.user.nickname + " says:" + message;
        System.out.println(message);

        // Send the private message to both users
        TCPServer.message(user, message);
        TCPServer.message(this.user.nickname, message);
    }
}