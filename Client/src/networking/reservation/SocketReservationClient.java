package networking.reservation;

import dtos.Request;
import dtos.reservation.ReservationRequest;
import dtos.vehicle.VehicleDisplayDto;
import model.entities.reservation.Reservation;
import networking.SocketService;

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
    Request request = new Request("reservation", "getVehicles", new Reservation() );
    return (List<VehicleDisplayDto>) SocketService.sendRequest(request);
  }
}
