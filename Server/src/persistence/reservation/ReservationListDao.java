package persistence.reservation;

import model.entities.reservation.Reservation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReservationListDao implements ReservationDao
{

  private final static List<Reservation> reservations = new ArrayList<>(
      Arrays.asList());

  public ReservationListDao()
  {

  }

  public void add(Reservation reservation)
  {
    reservations.add(reservation);
  }

  public ArrayList<Reservation> getByDate(model.Date date)
  {
    ArrayList<Reservation> results = new ArrayList<>();
    for (Reservation reservation : reservations)
    {
      if (reservation.getStartDate().equals(date) || reservation.getEndDate()
          .equals(date))
      {
        results.add(reservation);
      }
    }
    return results;
  }

  public void delete(Reservation reservation)
  {
    reservations.remove(reservation);
  }

  public void save(Reservation reservation, Reservation oldReservation)
  {
    delete(oldReservation);
    add(reservation);
    System.out.println("updated: " + reservation);
  }

  public ArrayList<Reservation> getByReserveEmail(String reservedEmail)
  {
    ArrayList<Reservation> results = new ArrayList<>();
    for (Reservation reservation : reservations)
    {
      if (reservation.getReservedByEmail().equals(reservedEmail))
      {
        results.add(reservation);
      }
    }
    return results;
  }

  public ArrayList<Reservation> getAll()
  {
    ArrayList<Reservation> results = new ArrayList<>();
    for (Reservation reservation : reservations)
    {
      results.add(reservation);
    }
    return results;
  }
  public ArrayList<Reservation> getByType(String vehicleType)
  {
    ArrayList<Reservation> results = new ArrayList<>();
    for (Reservation reservation : reservations)
    {
      if (reservation.getVehicleType().equals(vehicleType))
      {
        results.add(reservation);
      }
    }
    return results;
  }

  @Override public ArrayList<Reservation> getByTypeAndId(int vehicleId,
      String vehicleType) throws SQLException
  {
    return null;
  }
}
