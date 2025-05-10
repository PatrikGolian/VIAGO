package networking.addnew;

import dtos.Request;
import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;
import networking.SocketService;

public class SocketAddNewVehicleClient implements AddNewVehicleClient
{
  @Override public void addNewVehicle(AddNewBikeRequest newVehicle)
  {
    Request request = new Request("addVehicle", "addBike", newVehicle);
    System.out.println("DEBUG: Received AddNewVehicleRequest: " + request);
    SocketService.sendRequest(request);

  }

  @Override public void addNewVehicle(AddNewEBikeRequest newVehicle)
  {
    Request request = new Request("addVehicle", "addEBike", newVehicle);
    System.out.println("DEBUG: Received AddNewVehicleRequest: " + request);
    SocketService.sendRequest(request);
  }

  @Override public void addNewVehicle(AddNewScooterRequest newVehicle)
  {
    Request request = new Request("addVehicle", "addScooter", newVehicle);
    System.out.println("DEBUG: Received AddNewVehicleRequest: " + request);
    SocketService.sendRequest(request);
  }
}
