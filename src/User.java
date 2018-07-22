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

    // help from looking at Panos' code - my method was overly complex for no reason!
    public void changeNickname (String newNickname){
        this.nickname = newNickname;
    }
}
