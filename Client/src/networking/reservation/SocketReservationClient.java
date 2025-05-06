package networking.reservation;

import dtos.Request;
import dtos.reservation.ReservationRequest;
import dtos.vehicle.AddNewVehicleRequest;
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
    Request request = new Request("reservation", "view_vehicles", new AddNewVehicleRequest()
    {
    });
    //return (List<VehicleDisplayDto>) SocketService.sendRequest(request);

    List<VehicleDisplayDto> response = (List<VehicleDisplayDto>) SocketService.sendRequest(request);

    System.out.println("Received object: " + response);
    System.out.println("Received type: " + (response != null ? response.getClass().getName() : "null"));

    if (response instanceof List<?>) {
      return response;
    } else {
      throw new RuntimeException("Unexpected response: " + response);
    }
  }
}
