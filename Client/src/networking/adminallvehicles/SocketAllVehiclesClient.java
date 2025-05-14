package networking.adminallvehicles;

import dtos.Request;
import dtos.vehicle.DeleteVehicleRequest;
import dtos.vehicle.VehicleDisplayDto;
import networking.SocketService;

import java.util.List;

public class SocketAllVehiclesClient implements AdminAllVehiclesClient
{
  @Override public List<VehicleDisplayDto> getVehicles()
  {
    Request request = new Request("allVehicles", "view_vehicles", null);
    return (List<VehicleDisplayDto>) SocketService.sendRequest(request);
  }

  @Override public void delete(DeleteVehicleRequest payload)
  {
    Request request = new Request("allVehicles","delete_vehicle",payload);
    SocketService.sendRequest(request);
  }
}
