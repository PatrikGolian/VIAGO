package networking.reservation;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationRequestByIdType;
import dtos.vehicle.VehicleDisplayDto;

import java.util.List;

public interface ReservationClient
{
  void updateVehicleState();
  void addNewReservation(ReservationRequest newReservation);
  List<VehicleDisplayDto> getVehicles();
  List<ReservationDto> getReservationsByTypeAndId(
      ReservationRequestByIdType request);
}
