import java.net.Socket;
import java.util.UUID;

public class User {
    public float id;
    public String nickname;
    public Socket socket;

    public User(String nickname, Socket socket) {
        this.id = (float) (Math.random()*1);
        this.nickname = nickname;
        this.socket = socket;
    }

    public String toString() {
        return "[" + this.id + "](" + this.nickname + ")";
    }
}
