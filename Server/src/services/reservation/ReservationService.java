package services.reservation;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationRequestByIdType;
import dtos.reservation.ReservationReserveRequest;

import java.sql.SQLException;
import java.util.List;

public interface ReservationService
{
  void updateVehicleState();
  void addNewReservation(ReservationRequest request);
  void deleteAll(ReservationReserveRequest request);

  List<ReservationDto> getReservationsByTypeAndId(ReservationRequestByIdType request) throws SQLException;
}
