import java.io.*;

public class ConnectionHandler implements Runnable {
    public User user;

    //for nickname method - referred to Panos code and found this source for making String array
    //https://alvinalexander.com/java/java-string-array-reference-java-5-for-loop-syntax
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
                //approach inspired by Panos code and helped with info from
                // https://alvinalexander.com/java/java-string-array-reference-java-5-for-loop-syntax
               String[] wordsLine = line.split(" ");
               String newNickname = wordsLine[1];
                this.user.changeNickname(newNickname);
                response = "Hello " + this.user.nickname;
            } else if(line.startsWith("@dm")){
                response = directMessage(line);
            }
            TCPServer.broadcast(this.user.toString() + ": " + response);
        }
        return "keep talking!";
    }

    public String listUsers (String line){
        String response = "";
        for (User user : TCPServer.connections) {
            response += "\n" + user.toString() + "\n";
        }
        return response;
    }

    public String directMessage (String line){
        String[] wordLine = line.split("");
        String receiver = wordLine[1];
        String revisedMessage = wordline[wordLine.length-2];
        TCPServer.message(this.user.nickname, line);
        TCPServer.message (receiver, revisedMessage);
        TCPServer.broadcast(this.user.toString() + ": " + revisedMessage);
        return this.user.nickname + "chats to: "+ receiver + revisedMessage;
    }
}


//idea for creating a manual for the chat program in handleMessage method - ran out of time
//preserving start of code only as reminder for potential future update
//            }else if (line.startsWith(@man)){
//            response = manInfo(line);}