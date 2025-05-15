package persistence.reservation;

import model.entities.reservation.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationPostgresDao implements ReservationDao
{
  private static ReservationPostgresDao instance;

  private ReservationPostgresDao() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  private static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=viago",
        "postgres", "password");
  }

  public static synchronized ReservationPostgresDao getInstance()
      throws SQLException
  {
    if (instance == null)
    {
      instance = new ReservationPostgresDao();
    }
    return instance;
  }

  public Reservation create(Reservation reservation) throws SQLException
  {
    int vehicleId = reservation.getVehicleId();
    String ownerEmail = reservation.getOwnerEmail(), reservedByEmail = reservation.getReservedByEmail(), vehicleType = reservation.getVehicleType();
    double price = reservation.getPrice();
    PreparedStatement statement;
    java.sql.Date sqlStartDate = reservation.getStartDate().toSQLDate();
    java.sql.Date sqlEndDate = reservation.getEndDate().toSQLDate();

    try (Connection connection = getConnection())
    {
      statement = connection.prepareStatement(
          "INSERT INTO reservation(vehicleId, vehicleType, ownerEmail, reservedByEmail, startDate, endDate, price) VALUES(?,?,?,?,?,?,?)");

      statement.setInt(1, vehicleId);
      statement.setString(2, vehicleType);
      statement.setString(3, ownerEmail);
      statement.setString(4, reservedByEmail);
      statement.setDate(5, sqlStartDate);
      statement.setDate(6, sqlEndDate);
      statement.setDouble(7, price);

      statement.executeUpdate();
      return new Reservation(vehicleId, vehicleType, ownerEmail,
          reservedByEmail, reservation.getStartDate(), reservation.getEndDate(),
          price);

    }
  }

  public void add(Reservation reservation) throws SQLException
  {
    create(reservation);
  }

  public ArrayList<Reservation> getByDate(model.Date date)
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM reservation WHERE startDate = ? OR endDate = ?");
      java.sql.Date dated = date.toSQLDate();
      statement.setDate(1, dated);
      statement.setDate(2, dated);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Reservation> result = new ArrayList<>();
      while (resultSet.next())
      {
        int vehicleId = resultSet.getInt("vehicleId");
        String ownerEmail = resultSet.getString(
            "ownerEmail"), reservedByEmail = resultSet.getString(
            "reservedByEmail"), vehicleType = resultSet.getString(
            "vehicleType");
        double price = resultSet.getDouble("price");
        java.sql.Date date1 = resultSet.getDate(
            "startDate"), date2 = resultSet.getDate("endDate");
        LocalDate temp = date1.toLocalDate();
        model.Date startDate = new model.Date(temp.getDayOfMonth(), temp.getMonthValue(),
            temp.getYear());
        temp = date2.toLocalDate();
        model.Date endDate = new model.Date(temp.getDayOfMonth(), temp.getMonthValue(),
            temp.getYear());
        result.add(
            new Reservation(vehicleId, vehicleType, ownerEmail, reservedByEmail,
                startDate, endDate, price));
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
    java.sql.Date date = startDate.toSQLDate();
    java.sql.Date date2 = endDate.toSQLDate();

    PreparedStatement statement;
    try (Connection connection = getConnection())
    {
      statement = connection.prepareStatement(
          "DELETE FROM reservation WHERE vehicleId = ? AND startDate = ? AND endDate = ?");
      statement.setInt(1, vId);
      statement.setDate(2, date);
      statement.setDate(3, date2);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  public void deleteAll(String email)
  {
    PreparedStatement statement;
    try (Connection connection = getConnection())
    {
      statement = connection.prepareStatement(
          "DELETE FROM reservation WHERE reservedByEmail = ?");
      statement.setString(1, email);
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
    String ownerEmail = reservation.getOwnerEmail(), reservedByEmail = reservation.getReservedByEmail(), vehicleType = reservation.getVehicleType();
    double price = reservation.getPrice();
    java.sql.Date sqlStartDate = reservation.getStartDate().toSQLDate();
    java.sql.Date sqlEndDate = reservation.getEndDate().toSQLDate();
    java.sql.Date oldSqlStartDate = oldReservation.getStartDate().toSQLDate();
    java.sql.Date oldSqlEndDate = oldReservation.getEndDate().toSQLDate();

    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE reservation SET vehicleId = ?, vehicleType = ?, ownerEmail = ?, reservedByEmail = ?, startDate = ?, endDate = ?, price = ?  WHERE vehicleId = ? AND startDate = ? AND endDate = ?");
      statement.setInt(1, vehicleId);
      statement.setString(2, vehicleType);
      statement.setString(3, ownerEmail);
      statement.setString(4, reservedByEmail);
      statement.setDate(5, sqlStartDate);
      statement.setDate(6, sqlEndDate);
      statement.setDouble(7, price);

      statement.setInt(8, oldVehicleId);
      statement.setDate(9, oldSqlStartDate);
      statement.setDate(10, oldSqlEndDate);
      statement.executeUpdate();
    }
  }

  public ArrayList<Reservation> getByReserveEmail(String reservedByEmail)
      throws SQLException
  {

    ArrayList<Reservation> reservations = new ArrayList<>();

    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM reservation where reservedByEmail = ?");
      statement.setString(1, reservedByEmail);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        String ownerEmail = resultSet.getString(
            "ownerEmail"), vehicleType = resultSet.getString("vehicleType");
        int vehicleId = resultSet.getInt("vehicleId");
        java.sql.Date date1 = resultSet.getDate("startDate");
        LocalDate temp = date1.toLocalDate();
        model.Date startDate = new model.Date(temp.getDayOfMonth(), temp.getMonthValue(),
            temp.getYear());
        java.sql.Date date2 = resultSet.getDate("endDate");
        temp = date2.toLocalDate();
        model.Date endDate = new model.Date(temp.getDayOfMonth(), temp.getMonthValue(),
            temp.getYear());
        double price = resultSet.getDouble("price");

        reservations.add(
            new Reservation(vehicleId, vehicleType, ownerEmail, reservedByEmail,
                startDate, endDate, price));
      }
    }
    return reservations;
  }

  public ArrayList<Reservation> getByTypeAndId(int vehicleId, String vehicleType)
      throws SQLException
  {

    ArrayList<Reservation> reservations = new ArrayList<>();

    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM reservation where vehicleId = ? AND vehicleType = ?");
      statement.setInt(1, vehicleId);
      statement.setString(2, vehicleType);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        String ownerEmail = resultSet.getString(
            "ownerEmail"), reservedByEmail = resultSet.getString("reservedByEmail");
        java.sql.Date date1 = resultSet.getDate("startDate");
        LocalDate temp = date1.toLocalDate();
        model.Date startDate = new model.Date(temp.getDayOfMonth(), temp.getMonthValue(),
            temp.getYear());
        java.sql.Date date2 = resultSet.getDate("endDate");
        temp = date2.toLocalDate();
        model.Date endDate = new model.Date(temp.getDayOfMonth(), temp.getMonthValue(),
            temp.getYear());
        double price = resultSet.getDouble("price");

        reservations.add(
            new Reservation(vehicleId, vehicleType, ownerEmail, reservedByEmail,
                startDate, endDate, price));
      }
    }
    return reservations;
  }

  public ArrayList<Reservation> getAll() throws SQLException
  {
    ArrayList<Reservation> reservations = new ArrayList<>();

    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM reservation");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        String ownerEmail = resultSet.getString(
            "ownerEmail"), vehicleType = resultSet.getString(
            "vehicleType"), reservedByEmail = resultSet.getString(
            "reservedByEmail");
        int vehicleId = resultSet.getInt("vehicleId");
        java.sql.Date date1 = resultSet.getDate("startDate");
        LocalDate temp = date1.toLocalDate();
        model.Date startDate = new model.Date(temp.getDayOfMonth(), temp.getMonthValue(),
            temp.getYear());
        java.sql.Date date2 = resultSet.getDate("endDate");
        temp = date2.toLocalDate();
        model.Date endDate = new model.Date(temp.getDayOfMonth(), temp.getMonthValue(),
            temp.getYear());
        double price = resultSet.getDouble("price");

        reservations.add(
            new Reservation(vehicleId, vehicleType, ownerEmail, reservedByEmail,
                startDate, endDate, price));
      }
    }
    return reservations;
  }

}
