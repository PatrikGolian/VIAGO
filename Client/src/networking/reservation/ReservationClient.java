package networking.reservation;

import dtos.reservation.ReservationRequest;
import dtos.vehicle.VehicleDisplayDto;

import java.util.List;

public interface ReservationClient
{
  void addNewReservation(ReservationRequest newReservation);
  List<VehicleDisplayDto> getVehicles();
}
