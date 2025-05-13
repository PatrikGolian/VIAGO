package networking;

import networking.readerswriters.ReadWrite;
import startup.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
  private final ServiceProvider serviceProvider;
  private final ReadWrite sharedResource;

  public Server(ServiceProvider serviceProvider, ReadWrite sharedResource)
  {
    this.serviceProvider = serviceProvider;
    this.sharedResource = sharedResource;
  }

  public void start() throws IOException
  {
    ServerSocket serverSocket = new ServerSocket(2910);
    System.out.println("Server started, listening for connections...");
    while (true)
    {
      Socket socket = serverSocket.accept();
      MainSocketHandler socketHandler = new MainSocketHandler(socket,
          serviceProvider, sharedResource);
      Thread socketThread = new Thread(socketHandler);
      socketThread.start();
    }
  }
}
