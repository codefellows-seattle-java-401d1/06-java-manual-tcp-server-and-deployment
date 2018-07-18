import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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

        // Give options to users
        TCPServer.broadcast("Commands~" + "\n" + "@quit - quits chat" + "\n" + "@list - list connected users" + "\n" + "@nickname (your nickname) - change your username" + "\n" + "@dm (user id) - send a private message" + "\n");

        boolean isRunning = true;
        while (isRunning) {
            String line = buffer.readLine();
            String response = line + "\n";

            if (line.startsWith("@quit")) {
                isRunning = false;
                this.user.socket.close();
            } else if (line.startsWith("@list")) {
                response = "User Manifest~" + "\n" + listUsers(line);
            }
            // TODO: implement other command methods
            else if (line.startsWith("@nickname")) {
                response = "user #" + this.user.id + " changed their nickname to " + setNickname(line) + "\n";
            } else if (line.startsWith("@dm")) {

            }

            TCPServer.broadcast(this.user.toString() + ": " + response);
        }
    }

    public String listUsers (String line) {
        // TODO: implement list users [x]
        String response = "";

        for (User user : TCPServer.connections) {
            response += user.toString() + "\n";
        }

        return response;
    }

    public String setNickname (String line) {
        Scanner nicknameFinder = new Scanner(line);
        String nickname = nicknameFinder.next();
        nickname = nicknameFinder.next();
        this.user.nickname = nickname;
        return nickname;
    }
}
