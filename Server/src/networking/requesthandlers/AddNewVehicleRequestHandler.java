package networking.requesthandlers;

import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;
import dtos.vehicle.DeleteVehicleRequest;
import networking.readerswriters.ReadWrite;
import networking.readerswriters.Writer;
import services.vehicle.VehicleService;

import java.sql.SQLException;

public class AddNewVehicleRequestHandler implements RequestHandler
{
  private final VehicleService vehicleService;
  private final ReadWrite lock;

  public AddNewVehicleRequestHandler(VehicleService vehicleService, ReadWrite sharedResource)
  {
    this.vehicleService = vehicleService;
    this.lock = sharedResource;
  }

  @Override public Object handle(String action, Object payload)
      throws SQLException
  {
    switch (action)
    {
      case "addBike" -> {
        Writer writer = new Writer(lock, () -> {
          try
          {
            vehicleService.addNew((AddNewBikeRequest) payload);
          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
        Thread thread = new Thread(writer);
        thread.start();
        try {
          thread.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      case "addEBike" -> {
        Writer writer = new Writer(lock, () -> {
          try
          {
            vehicleService.addNew((AddNewEBikeRequest) payload);
          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
        Thread thread = new Thread(writer);
        thread.start();
        try {
          thread.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

      }
      case "addScooter" ->{
        Writer writer = new Writer(lock, () -> {
          try
          {
            vehicleService.addNew((AddNewScooterRequest) payload);
          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
        Thread thread = new Thread(writer);
        thread.start();
        try {
          thread.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

      }
    }
    return null;
  }
}
