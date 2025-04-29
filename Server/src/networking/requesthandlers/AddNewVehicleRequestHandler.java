package networking.requesthandlers;

import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;
import services.vehicle.VehicleService;

import java.sql.SQLException;

public class AddNewVehicleRequestHandler implements RequestHandler
{
  private final VehicleService vehicleService;

  public AddNewVehicleRequestHandler(VehicleService vehicleService)
  {
    this.vehicleService = vehicleService;
  }

  @Override public Object handle(String action, Object payload)
      throws SQLException
  {
    switch (action)
    {
      case "addBike" -> vehicleService.addNew((AddNewBikeRequest) payload);
      case "addEBike" -> vehicleService.addNew((AddNewEBikeRequest) payload);
      case "addScooter" -> vehicleService.addNew((AddNewScooterRequest) payload);
    }
    return null;
  }
}
