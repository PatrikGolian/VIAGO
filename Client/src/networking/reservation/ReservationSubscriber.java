package networking.reservation;
import dtos.Request;
import dtos.Response;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ReservationSubscriber {
  private final ObjectOutputStream out;
  private final ObjectInputStream in;
  private final Thread readerThread;

  public ReservationSubscriber(String host, int port, Runnable action) throws IOException
  {
    Socket sock = new Socket(host, port);
    this.out = new ObjectOutputStream(sock.getOutputStream());
    this.in = new ObjectInputStream(sock.getInputStream());

    out.writeObject(new Request("reservation", "subscribe", null));

    this.readerThread = new Thread(() -> {
      try {
        while (true) {
          Response response = (Response) in.readObject();
          System.out.println("----> [reservationSub] got push: " + response.status());
          switch (response.status()) {
            case "RESERVATION_ADDED":
            {
              Platform.runLater(action);
              System.out.println("----> [reservationSub] got reservation push! ");
            }
              break;
            case "BIKE_ADDED":
              Platform.runLater(action);
              break;
            case "EBIKE_ADDED":
              Platform.runLater(action);
              break;
            case "SCOOTER_ADDED":
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
