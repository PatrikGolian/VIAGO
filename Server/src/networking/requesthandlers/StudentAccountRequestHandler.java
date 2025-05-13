package networking.requesthandlers;

import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationRequestByIdType;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.BlacklistUserRequest;
import dtos.user.PromoteUserRequest;
import dtos.user.UpdatePasswordRequest;
import dtos.user.ViewUsers;
import networking.readerswriters.ReadWrite;
import networking.readerswriters.Reader;
import services.studentaccount.StudentAccountService;

import java.sql.SQLException;

public class StudentAccountRequestHandler implements RequestHandler
{
  private final StudentAccountService studentAccountService;
  private final ReadWrite lock;

  public StudentAccountRequestHandler(
      StudentAccountService studentAccountService, ReadWrite sharedResource)
  {
    this.studentAccountService = studentAccountService;
    this.lock = sharedResource;
  }
  public Object handle(String action, Object payload) throws SQLException
  {
    switch (action)
    {

      case "view_reservations" ->
      {
        Reader<Object> reader = new Reader<>(lock, () -> {
          try
          {
            return studentAccountService.getReservationsOverview((ReservationReserveRequest) payload);
          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
        Thread thread = new Thread(reader);
        thread.start();
        try
        {
          thread.join();
        }
        catch (InterruptedException e)
        {
          Thread.currentThread().interrupt();
        }
        return reader.getResult();

      }
      case "delete" -> {
        Reader<Object> reader = new Reader<>(lock, () -> {
          studentAccountService.delete((ReservationRequest) payload);
          return Boolean.TRUE;
        });
        Thread thread = new Thread(reader);
        thread.start();
        try
        {
          thread.join();
        }
        catch (InterruptedException e)
        {
          Thread.currentThread().interrupt();
        }
        return reader.getResult();

      }
      case "changeUser" -> {
        Reader<Object> reader = new Reader<>(lock, () -> {
          try
          {
            studentAccountService.changeUser((ChangeUserRequest) payload);
            return Boolean.TRUE;
          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
        Thread thread = new Thread(reader);
        thread.start();
        try
        {
          thread.join();
        }
        catch (InterruptedException e)
        {
          Thread.currentThread().interrupt();
        }
        return reader.getResult();

      }
      case "getPassword" -> {
        Reader<Object> reader = new Reader<>(lock, () -> {
          try
          {
            return studentAccountService.getPassword((GetPasswordRequest) payload);
          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
        Thread thread = new Thread(reader);
        thread.start();
        try
        {
          thread.join();
        }
        catch (InterruptedException e)
        {
          Thread.currentThread().interrupt();
        }
        return reader.getResult();
      }
    }
    return null;
  }
}
