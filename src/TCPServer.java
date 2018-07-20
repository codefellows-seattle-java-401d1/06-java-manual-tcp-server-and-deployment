// connect to this server using telnet:
// telnet localhost 6789

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class TCPServer {
    public static List<User> connections = new ArrayList<>();

    public static void broadcast(String message) {
        for (User user : connections) {
            try {
                DataOutputStream outToClient = new DataOutputStream(user.socket.getOutputStream());
                outToClient.writeBytes(message);
            } catch (IOException e) {

            }
        }
    }

    public static void message(String user, String message) {

    }

    // Starting the server obviously
    public static void main(String argv[]) throws Exception {
        int port = getPort();

        //Acknowledge a connection below to the server.
        System.out.println("Connecting to port " + port);
        try (ServerSocket welcomeSocket = new ServerSocket(port)) {
            boolean isRunning = true;
            Socket connectionSocket;
            while (isRunning) {
                System.out.print("Waiting for connection... ");
                connectionSocket = welcomeSocket.accept();

                User user = new User(1, "Unknown", connectionSocket);
                connections.add(user);
                System.out.print("Connection received! ");

                ConnectionHandler connectionHandler = new ConnectionHandler(user);
                (new Thread(connectionHandler)).start();
            }
        }
    }

    // Various connecting to, or not connecting message. Grabbing users port numbers.
    private static int getPort() {
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