package networking.myvehicles;

import dtos.Request;
import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationReserveRequest;
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
}
