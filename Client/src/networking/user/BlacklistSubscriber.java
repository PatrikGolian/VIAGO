package networking.user;

import dtos.Request;
import dtos.Response;
import dtos.user.BlackListDto;
import javafx.application.Platform;
import state.AppState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BlacklistSubscriber
{
  private final ObjectOutputStream out;
  private final ObjectInputStream in;
  private final Thread readerThread;

  public BlacklistSubscriber(String host, int port, Runnable action) throws
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
          Object raw = response.payload();
          if(raw instanceof BlackListDto)
          {
            BlackListDto blackListDto = (BlackListDto) raw;
            switch (response.status()) {
              case "USER_BLACKLISTED":
              {
                if(blackListDto.email().equals(AppState.getCurrentUser().email()))
                {
                  Platform.runLater(action);
                }
              }break;
              default:
            }
          }
        }
      } catch (Exception e) {
      }
    });
    readerThread.setDaemon(true);
    readerThread.start();
  }
}
