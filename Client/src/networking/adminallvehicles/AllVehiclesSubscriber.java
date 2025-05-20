package networking.adminallvehicles;

import dtos.Request;
import dtos.Response;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AllVehiclesSubscriber {
  private final ObjectOutputStream out;
  private final ObjectInputStream in;
  private final Thread readerThread;

  public AllVehiclesSubscriber(String host, int port, Runnable action) throws IOException
  {
    Socket sock = new Socket(host, port);
    this.out = new ObjectOutputStream(sock.getOutputStream());
    this.in = new ObjectInputStream(sock.getInputStream());

    out.writeObject(new Request("allVehicles", "subscribe", null));
    out.flush();

    this.readerThread = new Thread(() -> {
      try {
        while (true) {
          Response response = (Response) in.readObject();
          System.out.println("----> [allVehicles] got push: " + response.status());
          switch (response.status()) {
            case "RESERVATION_ADDED":
            case "BIKE_ADDED":
            case "EBIKE_ADDED":
            case "SCOOTER_ADDED":
            {
              Platform.runLater(action);
              System.out.println("----> [allVehicles] recieved the reservation push!");
            }
            break;
            case "USER_BLACKLISTED":
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

