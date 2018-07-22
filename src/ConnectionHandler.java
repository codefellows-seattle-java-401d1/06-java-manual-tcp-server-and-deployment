import java.io.*;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable {
    public User user;
    private String[] wordline = new String[2];

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

    public String handleMessage() throws IOException {
        InputStream inputStream = this.user.socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(reader);

        OutputStream outputStream = this.user.socket.getOutputStream();
        DataOutputStream backToClient = new DataOutputStream(outputStream);

        boolean isRunning = true;
        while (isRunning) {
            String line = buffer.readLine();
            String response = " back atcha, " + user.nickname + " \n";

            if (line.startsWith("@quit")) {
                isRunning = false;
                this.user.socket.close();
            } else if (line.startsWith("@list")) {
                response = listUsers(line);
            } else if (line.startsWith("@nickname")){
               String[] wordsLine = line.split(" ");
                String newNickname = wordsLine[1];
                this.user.changeNickname(newNickname);
                response = "Hello " + this.user.nickname;

            }
//            } else if(line.startsWith("@dm")){
//                response = directMessage(line);
//            }else if (line.startsWith(@man)){
//            response = manInfo(line);
//                }

            // TODO: implement other command methods

            TCPServer.broadcast(this.user.toString() + ": " + response);
        }
        return "keep talking!";
    }

    public String listUsers (String line) throws IOException{
        String response = "";
            for (User user : TCPServer.connections) {
                response += "\n" + user.toString() + "\n";
            }
            return response;
        }


//
//
//    public String directMessage (String line) throws IOException{ }
}
