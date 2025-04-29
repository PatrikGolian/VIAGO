package networking.addnew;

import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;

public interface AddNewVehicleClient
{
  public void addNewVehicle(AddNewBikeRequest newVehicle);
  public void addNewVehicle(AddNewEBikeRequest newVehicle);
  public void addNewVehicle(AddNewScooterRequest newVehicle);
}
