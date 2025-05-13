package services.studentaccount;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.ViewUsers;

import java.sql.SQLException;
import java.util.List;

public interface StudentAccountService
{
  List<ReservationDto> getReservationsOverview(ReservationReserveRequest request)
      throws SQLException;
  void changeUser(ChangeUserRequest request) throws SQLException;
  String getPassword(GetPasswordRequest request) throws SQLException;
}
