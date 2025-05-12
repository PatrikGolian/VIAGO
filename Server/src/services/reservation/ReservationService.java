package services.reservation;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationRequestByIdType;
import dtos.vehicle.VehicleDisplayDto;
import model.Date;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService
{
  void updateVehicleState();
  void addNewReservation(ReservationRequest request);

  List<ReservationDto> getReservationsByTypeAndId(ReservationRequestByIdType request) throws SQLException;
}
