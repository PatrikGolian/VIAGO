package services.reservation;

import dtos.reservation.ReservationRequest;
import model.Date;
import model.entities.reservation.Reservation;
import model.entities.vehicles.Vehicle;
import persistence.reservation.ReservationDao;
import persistence.vehicle.VehicleDao;

import java.sql.SQLException;
import java.util.ArrayList;

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
    ArrayList<Reservation> reservations = null;
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
      if (reservation.getStartDate().equals(today))
      {
        try
        {
          Vehicle vehicle = vehicleDao.getByIdAndType(
              reservation.getVehicleId(), reservation.getVehicleType());
          Vehicle temp = vehicle;
          vehicle.setState("RentedOut");
          vehicleDao.save(vehicle, temp);
        }
        catch (SQLException e)
        {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public void addNewReservation(ReservationRequest request)
  {
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
}
