package networking.reservation;

import dtos.Request;
import dtos.Response;
import dtos.reservation.ReservationRequest;
import dtos.vehicle.AddNewVehicleRequest;
import dtos.vehicle.VehicleDisplayDto;
import model.entities.reservation.Reservation;
import networking.SocketService;

import java.util.ArrayList;
import java.util.List;

public class SocketReservationClient implements ReservationClient
{
  @Override public void addNewReservation(ReservationRequest newReservation)
  {
    Request request = new Request("reservation", "reserve", newReservation);
    SocketService.sendRequest(request);
  }

  @Override public List<VehicleDisplayDto> getVehicles()
  {
    Request request = new Request("reservation", "view_vehicles", null);
    Object rawResponse = SocketService.sendRequest(request);

    if (!(rawResponse instanceof List<?> rawList))
    {
      throw new RuntimeException("Expected List, got: " + rawResponse);
    }

    List<VehicleDisplayDto> vehicles = new ArrayList<>();

    for (Object obj : rawList)
    {
      if (obj instanceof VehicleDisplayDto dto)
      {
        vehicles.add(dto);
      }
      else
      {
        throw new RuntimeException("Unexpected item in vehicle list: " + obj);
      }
    }
    return vehicles;
  }
}
