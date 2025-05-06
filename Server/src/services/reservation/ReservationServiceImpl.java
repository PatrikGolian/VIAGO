package services.reservation;

import dtos.reservation.ReservationRequest;
import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;
import dtos.vehicle.AddNewVehicleRequest;
import model.Date;
import model.entities.reservation.Reservation;
import model.entities.vehicles.Vehicle;
import persistence.daos.reservation.ReservationDao;
import persistence.daos.vehicle.VehicleDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class ReservationServiceImpl implements ReservationService
{
  private final ReservationDao reservationDao;
  private final VehicleDao vehicleDao;

  public ReservationServiceImpl(ReservationDao reservationDao, VehicleDao vehicleDao)
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
    for(Reservation reservation : reservations)
    {
      if(reservation.getStartDate().equals(today))
      {
        try
        {
          Vehicle vehicle = vehicleDao.getByIdAndType(reservation.getVehicleId(), reservation.getVehicleType());
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
    Date startDate = request.startDate(),
        endDate = request.endDate();


  }
}
