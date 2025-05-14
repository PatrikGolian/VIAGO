package networking.myvehicles;
import dtos.Request;
import dtos.Response;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MyVehiclesSubscriber {
  private final ObjectOutputStream out;
  private final ObjectInputStream in;
  private final Thread readerThread;

  public MyVehiclesSubscriber(String host, int port, Runnable action) throws IOException
  {
    Socket sock = new Socket(host, port);
    this.out = new ObjectOutputStream(sock.getOutputStream());
    this.in = new ObjectInputStream(sock.getInputStream());

    out.writeObject(new Request("yourVehicles", "subscribe", null));

    this.readerThread = new Thread(() -> {
      try {
        while (true) {
          Response response = (Response) in.readObject();
          switch (response.status()) {
            case "RESERVATION_ADDED":
              Platform.runLater(action);
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
