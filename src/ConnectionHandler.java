import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

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
                //users can change their nickname
                //call changeNickname method here
                String newNickname = changeNickname();

                backToClient.writeBytes("changed to..." + newNickname);
            }else if (line.startsWith("@dm")) {
                //send a message directly to another user by nickname
            }
            // TODO: implement other command methods

            TCPServer.broadcast(this.user.toString() + ": " + response);
        }
    }

    public String listUsers (String line) {
        // TODO: implement list users
        String response = "";

        for (User user : TCPServer.connections) {
            response += user.toString() + "\n";
        }

        return response;
    }


    public String changeNickname() throws IOException {

        System.out.print("Changed your nickname: ");
        InputStream inputStream = this.user.socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(reader);

        String line = "";
        boolean isRunning = true;
        while (isRunning) {
            line = buffer.readLine();
            System.out.println(line);
            isRunning = false;
        }

        return line;
    }
}
