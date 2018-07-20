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
                listUsers(line);
            } else if (line.startsWith("@nickname")) {
                //users can change their nickname
                //call changeNickname method here
                 changeNickname(line);
            }else if (line.startsWith("@dm")) {
                //send a message directly to another user by nickname
                directMessage(line);
            }
            TCPServer.broadcast(this.user.toString() + ": " + response);
        }
    }

    // This was on the TCP Server also and I brought it over here
    // Fleshed it out in class during code review
    public void listUsers (String line) {
        String userList = "";

        for (User user : TCPServer.connections) {
            userList += "@" + user.nickname + "\n";
        }
        TCPServer.message(this.user.nickname, userList);
    }

    // wrote this in class during code review. I did not get it on my own.
    // I was close and Brandon gave me suggestions but I didn't get it functional
    // on my own.
    public void changeNickname(String line) {
        String oldUserName = this.user.nickname;
        String[] cells = line.split(" ");

        if (cells.length > 1) {
            this.user.nickname = cells[1];
            TCPServer.message(this.user.nickname, "successfully changed name to @");
        } else {
            TCPServer.message(oldUserName, "error changing name");
        }

    }

    // wrote this in class during code review. I did not get it on my own.
    // FORMAT: [@dm <otherUSERName> <message>]
    private void directMessage(String line) {
        String[] cells = line.split(" ");
        String user = cells[1];

        int secondSpaceIndex = line.indexOf(" ", line.indexOf(" "));  //or 4
        String message = this.user.nickname + " says: " + line.substring(secondSpaceIndex);

        //send the private message to both users
        TCPServer.message(user, message);
        TCPServer.message(this.user.nickname, message);

    }
}
