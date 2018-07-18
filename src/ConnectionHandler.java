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

        boolean isRunning = true;
        while (isRunning) {
            String line = buffer.readLine();
            String response = line.toUpperCase() + "\n";

            if (line.startsWith("@quit")) {
                isRunning = false;
                this.user.socket.close();
            } else if (line.startsWith("@list")) {
                response = listUsers(line);
            } else if (line.startsWith("@nickname")) {
                response = nickname(name);

            } else if (line.startsWith("@dm")) {

            }
            // TODO: implement other command methods

            TCPServer.broadcast(this.user.toString() + ": " + response);
        }
    }

    public static String listUsers(String line) {
        String response = "";

        for (User user : TCPServer.connections) {
            response += user.toString() + "\n";
        }

        return response;
    }

    public String nickname(String name) {
        Scanner findNickname = new Scanner(name);
        String nickname = findNickname.next();

        User user1 = new User("hello", new Socket());
        String xs = user1.id;

        String response = " User's nickname is: " + xs + nickname;

        return response;
    }


}
