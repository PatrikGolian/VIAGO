package networking.studentaccount;

import dtos.Request;
import dtos.auth.RegisterUserRequest;
import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.ViewUsers;
import networking.SocketService;

import java.util.ArrayList;
import java.util.List;

public class SocketStudentAccountClient implements StudentAccountClient
{

  @Override public void changeUser(ChangeUserRequest user)
  {
    Request request = new Request("student", "changeUser", user);
    SocketService.sendRequest(request);

  }

  @Override public String getPassword(GetPasswordRequest email)
  {
    Request request = new Request("student","getPassword", email);

    return (String) SocketService.sendRequest(request);
  }

  @Override public void delete(ReservationRequest payload)
  {
    Request request = new Request("student","delete",payload);
    SocketService.sendRequest(request);
  }

  @Override
  public List<ReservationDto> getReservations(ReservationReserveRequest payload)
  {
    Request request = new Request("student", "view_reservations", payload);
    return (List<ReservationDto>) SocketService.sendRequest(request);
  }
}
