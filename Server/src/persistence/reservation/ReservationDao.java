package persistence.reservation;

import dtos.reservation.ReservationRequestByIdType;
import model.entities.reservation.Reservation;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ReservationDao
{
  void add(Reservation reservation) throws SQLException;
  ArrayList<Reservation> getByDate(model.Date date) throws SQLException;
  ArrayList<Reservation> getAll() throws SQLException;
  ArrayList<Reservation> getByReserveEmail(String reservedEmail)
      throws SQLException;
  ArrayList<Reservation> getByTypeAndId(int vehicleId, String vehicleType)
      throws SQLException;
  void delete(Reservation reservation) throws SQLException;
  void deleteAll(String request);
  void save(Reservation reservation, Reservation oldReservation)
      throws SQLException;
}
