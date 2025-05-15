package networking.user;

import dtos.Request;
import dtos.Response;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UsersSubscriber
{
  private final ObjectOutputStream out;
  private final ObjectInputStream in;
  private final Thread readerThread;

  public UsersSubscriber(String host, int port, Runnable action) throws
      IOException
  {
    Socket sock = new Socket(host, port);
    this.out = new ObjectOutputStream(sock.getOutputStream());
    this.in = new ObjectInputStream(sock.getInputStream());

    out.writeObject(new Request("users", "subscribe", null));

    this.readerThread = new Thread(() -> {
      try {
        while (true) {
          Response response = (Response) in.readObject();
          switch (response.status()) {
            case "USER_ADDED":
              Platform.runLater(action);
              break;
            case "USER_BLACKLISTED":
              Platform.runLater(action);
              break;
            default:
          }
        }
      } catch (Exception e) {
      }
    });
    readerThread.setDaemon(true);
    readerThread.start();
  }
}
