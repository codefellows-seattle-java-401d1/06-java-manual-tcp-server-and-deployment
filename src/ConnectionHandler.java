import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
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
                //users can change their nickname
                //call changeNickname method here
                 changeNickname(line);
                backToClient.writeBytes("changed to..." + changeNickname(line));
            }else if (line.startsWith("@dm")) {
                //send a message directly to another user by nickname
                directMessage(line);
            }
            TCPServer.broadcast(this.user.toString() + ": " + response);
        }
    }

    //This was on the TCP Server also and I brought it over here
    public String listUsers (String line) {
        String response = "";

        for (User user : TCPServer.connections) {
            response += user.toString() + "\n";
        }

        return response;
    }


    public String changeNickname(String nickname) throws IOException {

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

        Scanner findNickname = new Scanner (line);
        findNickname.next() //consume token of that line
        String userNickname = findNickname.next();
        String rest = findNickname.nextLine();

        //grab the user by the id number and change the nickname from "unknown" to "<new nickname>"
        //uses line 50 to broadcast back to the user the socket information and user.

        return line;
    }

    // wrote this in class during code review. I did not get it on my own.
    // FORMAT: [@dm <otherUSERName> <message>]
    private void directMessage(String line) {
        String[] cells = line.split(" ");
        String user = cells[1];

        int secondSpaceIndex = line.indexOf(" ", line.indexOf(" ");  //or 4
        String message = this.user.nickname + " says: " + line.substring(secondSpaceIndex);

        //send the private message to both users
        TCPServer.message(user, message);
        TCPServer.message(this.user.nickname, message);

    }
}
