package persistence.daos.reservation;

import model.entities.reservation.Reservation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ReservationDao
{
  void add(Reservation reservation) throws SQLException;
  ArrayList<Reservation> getByDate(Date date) throws SQLException;
  ArrayList<Reservation> getAll() throws SQLException;
  ArrayList<Reservation> getByReservEmail(String reservedEmail) throws SQLException;
  void delete(Reservation reservation) throws SQLException;
  void save(Reservation reservation, Reservation oldReservation) throws SQLException;
}
