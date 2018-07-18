import java.io.*;
import java.net.Socket;

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
                String newNickname = setNickname();
                response = "You updated your nickname to " + newNickname;
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
}
