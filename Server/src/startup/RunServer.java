package startup;

import networking.Server;
import networking.readerswriters.ReadWrite;
import networking.readerswriters.favorreaders.FavorReaders;
import services.reservation.ReservationService;
import services.scheduler.Scheduler;

import java.io.IOException;

public class RunServer
{

  public static void main(String[] args) throws IOException
  {
    ServiceProvider serviceLocator = new ServiceProvider();
    FavorReaders sharedResource = new FavorReaders();
    Scheduler scheduler = new Scheduler();
    scheduler.startSecondlyExecutorServiceCheck(
        ServiceProvider.getReservationService());
    Server server = new Server(serviceLocator, sharedResource);
    server.start();
  }
}
