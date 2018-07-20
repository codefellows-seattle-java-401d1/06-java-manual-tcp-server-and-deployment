import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class User {
    public String id;
    public String nickname;
    public Socket socket;

    public User(String nickname, Socket socket) {
        this.id = "" + Math.random();
        this.nickname = nickname;
        this.socket = socket;
    }

    public String toString() {
        return "[" + this.id + "](" + this.nickname + ")";
    }

    // wrote this in class during code review. I did not get it on my own.
    public void sendMessgae(String message) {
        try {
            DataOutputStream outToClient = new DataOutputStream(this.socket.getOutputStream());
            outToClient.writeBytes(message + "\n");
        } catch (IOException e) {

        }
    }

}
