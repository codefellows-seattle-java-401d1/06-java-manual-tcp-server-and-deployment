import java.net.Socket;
import java.util.UUID;

public class User {
    public String id;
    public String nickname;
    public Socket socket;

    public User(int id, String nickname, Socket socket) {
        this.id = "" + Math.random();
        this.nickname = nickname;
        this.socket = socket;
    }

    public String toString() {
        return "[" + this.id + "](" + this.nickname + ")";
    }

    public void sendMessageToServer(String message) {

    }

    public void changeNickname(String newName) {
        this.nickname = newName;
    }
}