package networking.requesthandlers;

import dtos.reservation.ReservationRequestByIdType;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.BlacklistUserRequest;
import dtos.user.PromoteUserRequest;
import dtos.user.UpdatePasswordRequest;
import dtos.user.ViewUsers;
import services.studentaccount.StudentAccountService;

import java.sql.SQLException;

public class StudentAccountRequestHandler implements RequestHandler
{
  private final StudentAccountService studentAccountService;

  public StudentAccountRequestHandler(
      StudentAccountService studentAccountService)
  {
    this.studentAccountService = studentAccountService;
  }
  public Object handle(String action, Object payload) throws SQLException
  {
    switch (action)
    {

      case "view_reservations" ->
      {
        return studentAccountService.getReservationsOverview((ReservationReserveRequest) payload);
      }
      case "changeUser" -> studentAccountService.changeUser((ChangeUserRequest) payload);
      case "getPassword" -> studentAccountService.getPassword((GetPasswordRequest) payload);
    }
    return null; // just a default return value. Some actions above may return stuff.
  }
}
