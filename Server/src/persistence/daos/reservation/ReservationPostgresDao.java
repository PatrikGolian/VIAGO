package persistence.daos.reservation;

import model.entities.User;
import model.entities.reservation.Reservation;
import model.entities.vehicles.Bike;
import model.entities.vehicles.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ReservationPostgresDao
{
  private static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=viago",
        "postgres", "password");
  }

  public Reservation create(Reservation reservation) throws SQLException
  {
    int vehicleId = reservation.getVehicleId();
    String ownerEmail = reservation.getOwnerEmail(), reservedByEmail = reservation.getReservedByEmail();
    model.Date startDate = reservation.getStartDate(), endDate = reservation.getEndDate();
    double price = reservation.getPrice();
    PreparedStatement statement;

    try (Connection connection = getConnection())
    {
      statement = connection.prepareStatement(
          "INSERT INTO reservation(vehicleId, ownerEmail, reservedByEmail, startDate, endDate, price) VALUES(?,?,?,?,?,?)" );

      statement.setInt(1,vehicleId);
      statement.setString(2,ownerEmail);
      statement.setString(3,reservedByEmail);
      java.sql.Date date = new java.sql.Date(startDate.getYear(),startDate.getMonth(),startDate.getDay());
      statement.setDate(4,date);
      java.sql.Date date2 = new java.sql.Date(endDate.getYear(),endDate.getMonth(),endDate.getDay());
      statement.setDate(5,date2);
      statement.setDouble(6,price);

      statement.executeUpdate();
      return new Reservation(vehicleId, ownerEmail, reservedByEmail, startDate, endDate, price);

    }
  }

  public void add(Reservation reservation) throws SQLException
  {
    create(reservation);
  }
  public ArrayList<Reservation> getByDate(Date date)
  {
    try(Connection connection = getConnection()){
      PreparedStatement statement = connection.prepareStatement("SELECT* FROM reservation WHERE startDate = ? OR endDate = ?");
      java.sql.Date dated = new java.sql.Date(date.getYear(), date.getMonth(), date.getDay());
      statement.setDate(1,dated);
      statement.setDate(2 ,dated);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Reservation> result = new ArrayList<>();
      while (resultSet.next()){
        int vehicleId = resultSet.getInt("vehicleId");
        String ownerEmail = resultSet.getString("ownerEmail"),
            reservedByEmail = resultSet.getString("reservedByEmail");
        double price = resultSet.getDouble("price");
        java.sql.Date date1 = resultSet.getDate("startDate"),
            date2 = resultSet.getDate("endDate");
        model.Date startDate = new model.Date(date1.getYear(),date1.getMonth(),date1.getDay());
        model.Date endDate = new model.Date(date2.getYear(),date2.getMonth(),date2.getDay());
        result.add(new Reservation(vehicleId, ownerEmail, reservedByEmail, startDate, endDate, price));
      }
      return result;
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
  public void delete(Reservation reservation)
  {
    int vId = reservation.getVehicleId();
    model.Date startDate = reservation.getStartDate(), endDate = reservation.getEndDate();
    java.sql.Date date = new java.sql.Date(startDate.getYear(),startDate.getMonth(),startDate.getDay());
    java.sql.Date date2 = new java.sql.Date(endDate.getYear(),endDate.getMonth(),endDate.getDay());

    PreparedStatement statement;
    try(Connection connection = getConnection())
    {
      statement = connection.prepareStatement("DELETE FROM reservation WHERE vehicleId = ?, startDate = ?, endDate = ?");
      statement.setInt(1, vId);
      statement.setDate(2, date);
      statement.setDate(3,date2);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
  public void save(Reservation reservation, Reservation oldReservation)
      throws SQLException
  {
    int vehicleId = reservation.getVehicleId(), oldVehicleId = oldReservation.getVehicleId();
    String ownerEmail = reservation.getOwnerEmail(),
        reservedByEmail = reservation.getReservedByEmail();
    Double price = reservation.getPrice();
    model.Date startDate = reservation.getStartDate(),
        endDate = reservation.getEndDate(),
        oldStartDate = oldReservation.getStartDate(),
        oldEndDate = oldReservation.getEndDate();

    try(Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE reservation SET vehicleId = ?, ownerEmail = ?, reservedByEmail = ?, startDate = ?, endDate = ?, price = ?  WHERE vehicleId = ?, startDate = ?, endDate = ?");
      statement.setInt(1,vehicleId);
      statement.setString(2,ownerEmail);
      statement.setString(3,reservedByEmail);
      java.sql.Date date = new java.sql.Date(startDate.getYear(),startDate.getMonth(),startDate.getDay());
      statement.setDate(4,date);
      java.sql.Date date2 = new java.sql.Date(endDate.getYear(),endDate.getMonth(),endDate.getDay());
      statement.setDate(5,date2);
      statement.setDouble(6,price);

      statement.setInt(1,oldVehicleId);
      java.sql.Date date3 = new java.sql.Date(oldStartDate.getYear(),oldStartDate.getMonth(),oldStartDate.getDay());
      statement.setDate(4,date3);
      java.sql.Date date4 = new java.sql.Date(oldEndDate.getYear(),oldEndDate.getMonth(),oldEndDate.getDay());
      statement.setDate(5,date4);
      statement.executeUpdate();
    }
  }

  ArrayList<Reservation> getByReservEmail(String reservedByEmail) throws SQLException
  {
    ArrayList<Reservation> reservations = null;

    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("SELECT* FROM reservation where reservedByEmail = ?");
      statement.setString(1, reservedByEmail);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        String ownerEmail = resultSet.getString("ownerEmail");
        int vehicleId = resultSet.getInt("vehicleId");
        java.sql.Date date1 = resultSet.getDate("startDate");
        model.Date startDate = new model.Date(date1.getYear(),date1.getMonth(),date1.getDay());
        java.sql.Date date2 = resultSet.getDate("endDate");
        model.Date endDate = new model.Date(date2.getYear(),date2.getMonth(),date2.getDay());
        double price = resultSet.getDouble("price");

        reservations.add(new Reservation(vehicleId, ownerEmail, reservedByEmail, startDate, endDate, price));
      }
    }

    return reservations;
  }

  ArrayList<Reservation> getAll() throws SQLException
  {
    ArrayList<Reservation> reservations = null;

    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("SELECT* FROM reservation ");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        String ownerEmail = resultSet.getString("ownerEmail"),
        reservedByEmail = resultSet.getString("reservedByEmail");
        int vehicleId = resultSet.getInt("vehicleId");
        java.sql.Date date1 = resultSet.getDate("startDate");
        model.Date startDate = new model.Date(date1.getYear(),date1.getMonth(),date1.getDay());
        java.sql.Date date2 = resultSet.getDate("endDate");
        model.Date endDate = new model.Date(date2.getYear(),date2.getMonth(),date2.getDay());
        double price = resultSet.getDouble("price");

        reservations.add(new Reservation(vehicleId, ownerEmail, reservedByEmail, startDate, endDate, price));
      }
    }

    return reservations;
  }

}
