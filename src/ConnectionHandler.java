import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;

public class ConnectionHandler implements Runnable {
    public User user;

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
            String response = line + " back atcha, " + user.nickname + " \n";

            if (line.startsWith("@quit")) {
                isRunning = false;
                this.user.socket.close();
            } else if (line.startsWith("@list")) {
                response = listUsers(line);
            }
//            } else if (line.startsWith("@nickname")){
//                response = changeNickname(line);
//            } else if(line.startsWith("@dm")){
//                response = directMessage(line);
//            }else if (line.startsWith(@man)){
//            response = manInfo(line);
//                }

            // TODO: implement other command methods

            TCPServer.broadcast(this.user.toString() + ": " + response);
        }

    }

    public String listUsers (String line) throws IOException{
        String response = "";
            for (User user : TCPServer.connections) {
                response += user.toString() + "\n";
            }
            return response;
        }


    public String changeNickname (String line) throws IOException{

        String newNickname = this.user.nickname;
        InputStream inputStream = this.user.socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(reader);

        boolean isRunning = true;
        while (isRunning) {
             line = buffer.readLine();
            System.out.println(line);
            isRunning = false;
        }
       newNickname = buffer;

        for (user: TCPServer.connections) {
            if (user.nickname != newNickname){
                user++;
            } else {
                this.user.nickname = newNickname
            }
        }
        String response = this.user.nickame;
        OutputStream outputStream = response.getOutputStream();
        DataOutputStream backToClient = new DataOutputStream(outputStream);
            return response;
      }
//
//
//    public String directMessage (String line) throws IOException{ }
}
