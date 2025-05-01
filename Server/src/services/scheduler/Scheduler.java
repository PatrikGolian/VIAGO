package services.scheduler;

import model.Date;
import services.reservation.ReservationService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler
{
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public void startHourlyExecutorServiceCheck(ReservationService reservationService)
  {
    scheduler.scheduleAtFixedRate(reservationService::updateVehicleState,0,1, TimeUnit.HOURS);
  }

  public void stop()
  {
    scheduler.shutdown();
  }
}
