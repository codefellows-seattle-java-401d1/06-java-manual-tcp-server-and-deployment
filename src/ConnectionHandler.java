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
                response = setNickname(line);
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

    public String setNickname(String newNickname) throws IOException {
        // Get current nickname
        InputStream inputStream = this.user.socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(reader);

        boolean isRunning = true;
        while (isRunning) {
            String line = buffer.readLine();
            System.out.println(line);
            isRunning = false;
        }
        // Assign current nickname to variable
//        String originName = this.nickname.toString();

        // User to change variable to new nickname


        // Feedback to user that new nickname has been set
        // Return nickname variable
        return newNickname;
    }
}
