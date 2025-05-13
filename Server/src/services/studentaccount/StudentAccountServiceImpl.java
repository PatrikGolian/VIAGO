package services.studentaccount;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import model.entities.User;
import model.entities.reservation.Reservation;
import model.exceptions.ValidationException;
import persistence.reservation.ReservationDao;
import persistence.user.UserDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentAccountServiceImpl implements StudentAccountService
{
  private final ReservationDao reservationDao;
  private final UserDao userDao;

  public StudentAccountServiceImpl(ReservationDao reservationDao,
      UserDao userDao)
  {
    this.reservationDao = reservationDao;
    this.userDao = userDao;
  }

  @Override public List<ReservationDto> getReservationsOverview(
      ReservationReserveRequest reservedByEmail) throws SQLException
  {
    List<Reservation> reservations = reservationDao.getByReserveEmail(
        reservedByEmail.reservedByEmail());
    List<ReservationDto> result = new ArrayList<>();

    for (Reservation reservation : reservations)
    {
      {
        ReservationDto dto = new ReservationDto(reservation.getVehicleId(),
            reservation.getVehicleType(), reservation.getOwnerEmail(),
            reservation.getReservedByEmail(), reservation.getStartDate(),
            reservation.getEndDate(), reservation.getPrice());
        result.add(dto);
      }
    }
    return result;
  }

  @Override public void changeUser(ChangeUserRequest request)
      throws SQLException
  {
    User user = userDao.getSingle(request.email());

    if(user == null)
    {
      throw new ValidationException("User not found");
    }

    userDao.updateName(request.email(),request.firstName(), request.lastName());

    String newPassword = request.password();

    if(newPassword != null && !newPassword.isBlank() && !newPassword.equals(user.getPassword()))
    {
      userDao.updatePassword(request.email(), newPassword);
    }
  }

  @Override public String getPassword(GetPasswordRequest request)
      throws SQLException
  {
    User user = userDao.getSingle(request.email());
    return user.getPassword();
  }

  @Override public void delete(ReservationRequest request)
  {
    LocalDate today = LocalDate.now();
    LocalDate start = LocalDate.of(request.startDate().getYear(),
        request.startDate().getMonth(), request.startDate().getDay());

    if (today.isBefore(start))
    {
      try
      {
        Reservation reservation = new Reservation(request.vehicleId(),
            request.vehicleType(), request.ownerEmail(),
            request.reservedByEmail(), request.startDate(), request.endDate(),
            request.price());
        reservationDao.delete(reservation);
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
    }
    else if (!today.isBefore(start))
    {
      throw new IllegalArgumentException(
          "You cannot delete past or current reservations.");
    }
  }
}
