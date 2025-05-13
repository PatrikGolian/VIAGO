package networking.studentaccount;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.ViewUsers;

import java.util.List;

public interface StudentAccountClient
{
  List<ReservationDto> getReservations(ReservationReserveRequest request);
  void changeUser(ChangeUserRequest request);
  String getPassword(GetPasswordRequest request);
  void delete(ReservationRequest request);
}
