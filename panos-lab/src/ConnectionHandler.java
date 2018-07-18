import java.io.*;

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
            String response = line + "\n";

            if (line.startsWith("@")) {
                String[] parsedString = line.split(" ");
                switch (parsedString[0]) {
                    case "@dm":
                        String message = "";
                        for (int i = 2; i < parsedString.length; i++) {
                            message += parsedString[i];
                        }
                        TCPServer.privateSend(message, parsedString[1]);
                        break;
                    case "@nickname":
                        String newNick = "";
                        for (int i = 0; i < parsedString.length - 1; i++) {
                            newNick += parsedString[i];
                        }
                        this.user.nickname = newNick;
                        break;
                    case "@list":
                        try{
                            backToClient.writeBytes(listUsers());
                        }
                        catch (IOException e) {}
                            break;
                    case "@quit":
                        isRunning = false;
                        this.user.socket.close();
                        break;
                    default:
                        TCPServer.broadcast(this.user.toString() + ": " + response, this.user.nickname);
                        break;
                    }
                }
            }
        }


    public String listUsers () {
            int userCount = 0;
            String response = "";
        for(User user : TCPServer.connections){
            response += userCount + ". " + user.nickname +"\n";
        }
        return(response);
    }
}
