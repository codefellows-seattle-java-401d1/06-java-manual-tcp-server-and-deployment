import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class TCPServer {
    public static List<User> connections = new ArrayList<>();

    public static void broadcast(String message) {
        for (User user : connections) {
            try {
                DataOutputStream outToClient = new DataOutputStream(user.socket.getOutputStream());
                outToClient.writeBytes(message + "\n");
            } catch (IOException e) {

            }
        }
    }

    public static void message(String nickname, String message) {
        for (User user : connections) {
            try {
                if (user.nickname.equals(nickname)) {
                    DataOutputStream outToClient = new DataOutputStream(user.socket.getOutputStream());
                    outToClient.writeBytes(message + "\n");
                }
            } catch (IOException e) {

            }
        }
    }

    public static void removeUser(User user) {
        connections.remove(user);
    }

    public static void main(String argv[]) throws Exception {
        startServer();
    }

    public static void startServer() {
        int port = getPort();

        System.out.println("Connecting to port " + port);
        try (ServerSocket welcomeSocket = new ServerSocket(port)) {
            boolean isRunning = true;
            while (isRunning) {
                System.out.print("Waiting for connection... ");
                Socket connectionSocket = welcomeSocket.accept();

                User user = new User("unknown", connectionSocket);

                ConnectionHandler connection = new ConnectionHandler(user);
                (new Thread(connection)).start();

                connections.add(user);
                System.out.println("connection received!");

            }
        } catch (IOException e) {

        }
    }

    private static String listUsers(String line) {
        String response = "";

        for (User user : connections) {
            response += user.toString() + "\n";
        }

        return response;
    }

    public static int getPort() {
        int defaultPort = 6789;

        String portEnv = System.getenv("USER");
        if (portEnv != null) {
            try {
                return Integer.valueOf(System.getenv("PORT"));
            } catch (NumberFormatException e) {
                System.out.println("Invalid port: " + portEnv);
            }
        }
        return defaultPort;
    }
}