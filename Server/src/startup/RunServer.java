package startup;

import networking.Server;
import services.reservation.ReservationService;
import services.scheduler.Scheduler;

import java.io.IOException;

public class RunServer
{

  public static void main(String[] args) throws IOException
  {
    ServiceProvider serviceLocator = new ServiceProvider();
    Scheduler scheduler = new Scheduler();
    scheduler.startSecondlyExecutorServiceCheck(
        ServiceProvider.getReservationService());
    Server server = new Server(serviceLocator);
    server.start();
  }
}
