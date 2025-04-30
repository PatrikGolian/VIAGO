package services.reservation;

import dtos.reservation.ReservationRequest;
import model.Date;

import java.time.LocalDate;

public interface ReservationService
{
  void updateVehicleState(Date today);
  void addNewReservation(ReservationRequest request);
}
