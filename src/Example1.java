import com.vyze.Connection;
import com.vyze.MessageIn;
import com.vyze.MessageOut;
import org.java_websocket.WebSocketImpl;

/**
 * Created by Julian on 5/6/2015.
 */
public class Example1 {

    public static void main(String[] args) {

        WebSocketImpl.DEBUG = true;

        Connection c = new Connection() {
            @Override
            public void onOpen() {
                send(new MessageOut.CreateSession("username", "password"));
            }

            @Override
            public void onMessage(MessageIn msg) {
                System.out.println(msg);
                switch (msg.getCmd()) {
                    case "create_session": {
                        MessageIn.CreateSession msgc = (MessageIn.CreateSession) msg;
                        if (msgc.getSuccess()) {
                            System.out.println("Successfully logged in. SID: " + msgc.getSid());
                            send(new MessageOut.Session(msgc.getSid()));
                        } else {
                            System.out.println("Could not log in.");
                        }
                        break;
                    }
                    case "login": {
                        MessageIn.Login msgc = (MessageIn.Login) msg;
                        send(new MessageOut.LoadMiniworld(27));
                        break;
                    }
                    case "miniworlds": {
                        MessageIn.Miniworlds msgc = (MessageIn.Miniworlds) msg;
                        System.out.println(msgc.getMiniworlds().length + " miniworlds");
                        break;
                    }
                    case "create_miniworld": {
                        MessageIn.CreateMiniworld msgc = (MessageIn.CreateMiniworld) msg;
                        System.out.println(msgc.getMiniworld());
                        break;
                    }
                    case "miniworld": {
                        MessageIn.Miniworld msgc = (MessageIn.Miniworld) msg;
                        System.out.println(msgc.getMiniworld());
                        System.out.println(msgc.getClasses().length + " classes");
                        System.out.println(msgc.getFields().length + " fields");
                        System.out.println(msgc.getGroups().length + " groups");
                        break;
                    }
                }
            }

            @Override
            public void onClose() {

            }
        };

        c.connect("ws://vy-global.vyze.me:12345/ws");
    }

}
