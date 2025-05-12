package services.scheduler;

import model.Date;
import services.reservation.ReservationService;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler
{
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
      1);

  public void startSecondlyExecutorServiceCheck(ReservationService reservationService) {
    scheduler.scheduleAtFixedRate(() -> {
          try {
            System.out.println("[SCHEDULER] running updateVehicleState at "
                + LocalDateTime.now());
            reservationService.updateVehicleState();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        },
        0,
        10,
        TimeUnit.SECONDS    // or SECONDS if you really meant “every second”
    );
  }

  public void stop()
  {
    scheduler.shutdown();
  }
}
