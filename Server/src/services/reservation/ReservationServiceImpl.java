package services.reservation;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationRequestByIdType;
import dtos.user.ViewUsers;
import model.Date;
import model.entities.User;
import model.entities.reservation.Reservation;
import model.entities.vehicles.Vehicle;
import model.exceptions.ValidationException;
import persistence.reservation.ReservationDao;
import persistence.vehicle.VehicleDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService
{
  private final ReservationDao reservationDao;
  private final VehicleDao vehicleDao;

  public ReservationServiceImpl(ReservationDao reservationDao,
      VehicleDao vehicleDao)
  {
    this.reservationDao = reservationDao;
    this.vehicleDao = vehicleDao;
  }

  public void updateVehicleState()
  {
    Date today = Date.today();
    ArrayList<Reservation> reservations;

    try
    {
      reservations = reservationDao.getAll();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
    for (Reservation reservation : reservations)
    {
      LocalDate temporary = reservation.getEndDate().toLocalDate();
      temporary = temporary.plusDays(1);
      Date dayAfterEndDate = new Date(temporary.getDayOfMonth(), temporary.getMonthValue(), temporary.getYear());

      if (reservation.getStartDate().equals(today))
      {
        try
        {
          Vehicle vehicle = vehicleDao.getByIdAndType(
              reservation.getVehicleId(), reservation.getVehicleType());
          Vehicle temp = new Vehicle(vehicle);
          vehicle.setState("RentedOut");
          System.out.println(
              "[updateVehicleState] entered at " + LocalDateTime.now());

          vehicleDao.save(vehicle, temp);
        }
        catch (SQLException e)
        {
          throw new RuntimeException(e);
        }
      } else if (dayAfterEndDate.equals(today))
      {
        try
        {
          Vehicle vehicle = vehicleDao.getByIdAndType(
              reservation.getVehicleId(), reservation.getVehicleType());
          Vehicle temp = new Vehicle(vehicle);
          vehicle.setState("Available");
          System.out.println(
              "[updateVehicleState] entered at " + LocalDateTime.now());

          vehicleDao.save(vehicle, temp);
        }
        catch (SQLException e)
        {
          throw new RuntimeException(e);
        }
      } else
        System.out.println("[UPDATESTATE] ----> not equal");
    }
  }

  public void addNewReservation(ReservationRequest request)
  {
    validatePeriod(request.startDate(), request.endDate());

    Reservation reservation = new Reservation(request.vehicleId(),
        request.vehicleType(), request.ownerEmail(), request.reservedByEmail(),
        request.startDate(), request.endDate(), request.price());

    try
    {
      reservationDao.add(reservation);
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  private static void validatePeriod(Date startDate, Date endDate)
  {
    if (endDate.isBefore(startDate))
    {
      throw new ValidationException("The end date has to be after start date");
    }

  }

  @Override public List<ReservationDto> getReservationsByTypeAndId(
      ReservationRequestByIdType request) throws SQLException
  {

    List<Reservation> reservations = reservationDao.getByTypeAndId(request.vehicleId(),request.vehicleType());
    List<ReservationDto> result = new ArrayList<>();

    for (Reservation reservation : reservations)
    {
      if (reservation.getVehicleType().equals(request.vehicleType()) && reservation.getVehicleId() == request.vehicleId())
      {
        ReservationDto dto = new ReservationDto(reservation.getVehicleId(),
            reservation.getVehicleType(), reservation.getOwnerEmail(),
            reservation.getReservedByEmail(), reservation.getStartDate(),
            reservation.getEndDate(), reservation.getPrice());
        result.add(dto);
      }
    }
    return  result;
  }
}


