package services.studentaccount;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import model.entities.User;
import model.entities.reservation.Reservation;
import model.exceptions.ValidationException;
import persistence.reservation.ReservationDao;
import persistence.user.UserDao;

import java.sql.SQLException;
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
    User existingUser = userDao.getSingle(request.email());
    if (existingUser == null)
    {
      throw new ValidationException("User Not found!");
    }

    User newUser = new User(

        request.email(), request.password(), request.firstName(),
        request.lastName());

    userDao.change(newUser);
  }

  @Override public String getPassword(GetPasswordRequest request)
      throws SQLException
  {
    User user = userDao.getSingle(request.email());
    return user.getPassword();
  }
}
