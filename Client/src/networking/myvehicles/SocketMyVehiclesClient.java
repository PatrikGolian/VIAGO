package networking.myvehicles;

import dtos.Request;
import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.vehicle.DeleteVehicleRequest;
import dtos.vehicle.VehicleDisplayDto;
import dtos.vehicle.VehicleOwnerRequest;
import networking.SocketService;

import java.util.List;

public class SocketMyVehiclesClient implements MyVehiclesClient
{
  public List<VehicleDisplayDto> getVehicles(VehicleOwnerRequest payload)
  {
    Request request = new Request("yourVehicles", "view_vehicles", payload);
    return (List<VehicleDisplayDto>) SocketService.sendRequest(request);
  }
  @Override public void delete(DeleteVehicleRequest payload)
  {
    Request request = new Request("yourVehicles","delete_vehicle",payload);
    SocketService.sendRequest(request);
  }
}
