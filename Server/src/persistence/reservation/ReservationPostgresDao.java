package persistence.reservation;

import model.entities.reservation.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationPostgresDao implements ReservationDao {
  private static ReservationPostgresDao instance;

  private ReservationPostgresDao() throws SQLException {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=viago",
        "postgres", "password"
    );
  }

  public static synchronized ReservationPostgresDao getInstance() throws SQLException {
    if (instance == null) {
      instance = new ReservationPostgresDao();
    }
    return instance;
  }

  @Override
  public Reservation create(Reservation reservation) throws SQLException {
    String sql =
        "INSERT INTO reservation(vehicleId, reservedByEmail, startDate, endDate, price) " +
            "VALUES (?,?,?,?,?)";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, reservation.getVehicleId());
      ps.setString(2, reservation.getReservedByEmail());
      ps.setDate(3, reservation.getStartDate().toSQLDate());
      ps.setDate(4, reservation.getEndDate().toSQLDate());
      ps.setDouble(5, reservation.getPrice());
      ps.executeUpdate();
      return reservation;
    }
  }

  @Override
  public void add(Reservation reservation) throws SQLException {
    create(reservation);
  }


  private model.Date toModelDate(java.sql.Date d) {
    LocalDate ld = d.toLocalDate();
    return new model.Date(ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear());
  }

  @Override
  public ArrayList<Reservation> getByDate(model.Date date) {
    String sql =
        "SELECT r.vehicleId, r.reservedByEmail, r.startDate, r.endDate, r.price, " +
            "       v.type AS vehicleType, v.owneremail " +
            "FROM reservation r " +
            "  JOIN vehicle v ON r.vehicleId = v.id " +
            "WHERE r.startDate = ? OR r.endDate = ?";
    ArrayList<Reservation> result = new ArrayList<>();

    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      java.sql.Date d = date.toSQLDate();
      ps.setDate(1, d);
      ps.setDate(2, d);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          int vid = rs.getInt("vehicleId");
          String vtype = rs.getString("vehicleType");
          String ownerEmail = rs.getString("owneremail");
          String reservedBy = rs.getString("reservedByEmail");
          model.Date start = toModelDate(rs.getDate("startDate"));
          model.Date end = toModelDate(rs.getDate("endDate"));
          double price = rs.getDouble("price");

          result.add(new Reservation(
              vid, vtype, ownerEmail, reservedBy, start, end, price
          ));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  @Override
  public void delete(Reservation reservation) {
    String sql =
        "DELETE FROM reservation " +
            "WHERE vehicleId = ? AND startDate = ? AND endDate = ?";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, reservation.getVehicleId());
      ps.setDate(2, reservation.getStartDate().toSQLDate());
      ps.setDate(3, reservation.getEndDate().toSQLDate());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAll(String email) {
    String sql = "DELETE FROM reservation WHERE reservedByEmail = ?";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, email);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(Reservation reservation, Reservation oldReservation)
      throws SQLException {
    String sql =
        "UPDATE reservation SET " +
            "  reservedByEmail = ?, startDate = ?, endDate = ?, price = ? " +
            "WHERE vehicleId = ? AND startDate = ? AND endDate = ?";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, reservation.getReservedByEmail());
      ps.setDate(2, reservation.getStartDate().toSQLDate());
      ps.setDate(3, reservation.getEndDate().toSQLDate());
      ps.setDouble(4, reservation.getPrice());

      ps.setInt(5, oldReservation.getVehicleId());
      ps.setDate(6, oldReservation.getStartDate().toSQLDate());
      ps.setDate(7, oldReservation.getEndDate().toSQLDate());
      ps.executeUpdate();
    }
  }

  @Override
  public ArrayList<Reservation> getByReserveEmail(String reservedByEmail)
      throws SQLException {
    String sql =
        "SELECT r.vehicleId, r.reservedByEmail, r.startDate, r.endDate, r.price, " +
            "       v.type AS vehicleType, v.owneremail " +
            "FROM reservation r " +
            "  JOIN vehicle v ON r.vehicleId = v.id " +
            "WHERE r.reservedByEmail = ?";
    ArrayList<Reservation> list = new ArrayList<>();

    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, reservedByEmail);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          int vid = rs.getInt("vehicleId");
          String vtype = rs.getString("vehicleType");
          String ownerEmail = rs.getString("owneremail");
          model.Date start = toModelDate(rs.getDate("startDate"));
          model.Date end = toModelDate(rs.getDate("endDate"));
          double price = rs.getDouble("price");

          list.add(new Reservation(
              vid, vtype, ownerEmail, reservedByEmail, start, end, price
          ));
        }
      }
    }
    return list;
  }

  @Override
  public ArrayList<Reservation> getByTypeAndId(int vehicleId, String vehicleType)
      throws SQLException {
    String sql =
        "SELECT r.vehicleId, r.reservedByEmail, r.startDate, r.endDate, r.price, " +
            "       v.type AS vehicleType, v.owneremail " +
            "FROM reservation r " +
            "  JOIN vehicle v ON r.vehicleId = v.id " +
            "WHERE r.vehicleId = ? AND v.type = ?";
    ArrayList<Reservation> list = new ArrayList<>();

    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, vehicleId);
      ps.setString(2, vehicleType);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String ownerEmail = rs.getString("owneremail");
          String reservedBy = rs.getString("reservedByEmail");
          model.Date start = toModelDate(rs.getDate("startDate"));
          model.Date end = toModelDate(rs.getDate("endDate"));
          double price = rs.getDouble("price");

          list.add(new Reservation(
              vehicleId, vehicleType, ownerEmail, reservedBy, start, end, price
          ));
        }
      }
    }
    return list;
  }

  @Override
  public ArrayList<Reservation> getAll() throws SQLException {
    String sql =
        "SELECT r.vehicleId, r.reservedByEmail, r.startDate, r.endDate, r.price, " +
            "       v.type AS vehicleType, v.owneremail " +
            "FROM reservation r " +
            "  JOIN vehicle v ON r.vehicleId = v.id";
    ArrayList<Reservation> list = new ArrayList<>();

    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        int vid = rs.getInt("vehicleId");
        String vtype = rs.getString("vehicleType");
        String ownerEmail = rs.getString("owneremail");
        String reservedBy = rs.getString("reservedByEmail");
        model.Date start = toModelDate(rs.getDate("startDate"));
        model.Date end = toModelDate(rs.getDate("endDate"));
        double price = rs.getDouble("price");

        list.add(new Reservation(
            vid, vtype, ownerEmail, reservedBy, start, end, price
        ));
      }
    }
    return list;
  }
}
