package persistence.vehicle;

import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiclePostgresDao implements VehicleDao {
  private static VehiclePostgresDao instance;

  private VehiclePostgresDao() throws SQLException {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=viago",
        "postgres", "password"
    );
  }

  public static synchronized VehiclePostgresDao getInstance() throws SQLException {
    if (instance == null) {
      instance = new VehiclePostgresDao();
    }
    return instance;
  }

  public Vehicle create(Vehicle vehicle) throws SQLException {
    String sqlV = """
            INSERT INTO vehicle(type, brand, model, condition, color, pricePerDay, ownerEmail, state)
            VALUES (?,?,?,?,?,?,?,?)
            """;

    try (Connection conn = getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(sqlV, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, vehicle.getType());
        ps.setString(2, vehicle.getBrand());
        ps.setString(3, vehicle.getModel());
        ps.setString(4, vehicle.getCondition());
        ps.setString(5, vehicle.getColor());
        ps.setDouble(6, vehicle.getPricePerDay());
        ps.setString(7, vehicle.getOwnerEmail());
        ps.setString(8, vehicle.getState());
        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        if (!keys.next()) {
          conn.rollback();
          throw new SQLException("Vehicle insert failed, no ID obtained.");
        }
        int id = keys.getInt(1);

        switch (vehicle.getType()) {
          case "bike" -> {
            String sql = "INSERT INTO bike(vehicleId, bikeType) VALUES (?,?)";
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
              ps2.setInt(1, id);
              ps2.setString(2, ((Bike)vehicle).getBikeType());
              ps2.executeUpdate();
            }
            vehicle = new Bike(
                id, vehicle.getType(), vehicle.getBrand(), vehicle.getModel(),
                vehicle.getCondition(), vehicle.getColor(), vehicle.getPricePerDay(),
                ((Bike)vehicle).getBikeType(), vehicle.getOwnerEmail(), vehicle.getState()
            );
          }
          case "e-bike" -> {
            String sql = "INSERT INTO ebike(vehicleId, bikeType, maxSpeed, oneChargeRange) VALUES (?,?,?,?)";
            EBike eb = (EBike)vehicle;
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
              ps2.setInt(1, id);
              ps2.setString(2, eb.getBikeType());
              ps2.setInt(3, eb.getMaxSpeed());
              ps2.setInt(4, eb.getOneChargeRange());
              ps2.executeUpdate();
            }
            vehicle = new EBike(
                id, vehicle.getType(), vehicle.getBrand(), vehicle.getModel(),
                vehicle.getCondition(), vehicle.getColor(), vehicle.getPricePerDay(),
                eb.getBikeType(), eb.getMaxSpeed(), eb.getOneChargeRange(),
                vehicle.getOwnerEmail(), vehicle.getState()
            );
          }
          case "scooter" -> {
            String sql = "INSERT INTO scooter(vehicleId, maxSpeed, oneChargeRange) VALUES (?,?,?)";
            Scooter sc = (Scooter)vehicle;
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
              ps2.setInt(1, id);
              ps2.setInt(2, sc.getMaxSpeed());
              ps2.setInt(3, sc.getOneChargeRange());
              ps2.executeUpdate();
            }
            vehicle = new Scooter(
                id, vehicle.getType(), vehicle.getBrand(), vehicle.getModel(),
                vehicle.getCondition(), vehicle.getColor(), vehicle.getPricePerDay(),
                sc.getMaxSpeed(), sc.getOneChargeRange(),
                vehicle.getOwnerEmail(), vehicle.getState()
            );
          }
          default -> throw new SQLException("Unknown vehicle type: " + vehicle.getType());
        }

        conn.commit();
        return vehicle;
      } catch (SQLException ex) {
        conn.rollback();
        throw ex;
      }
    }
  }

  @Override
  public void add(Vehicle vehicle) throws SQLException {
    create(vehicle);
  }

  @Override
  public ArrayList<Vehicle> getByType(String type) throws SQLException {
    return switch (type) {
      case "bike"    -> getBike();
      case "e-bike"  -> getEBike();
      case "scooter" -> getScooter();
      default        -> new ArrayList<>();
    };
  }

  @Override
  public ArrayList<Vehicle> getByState(String state) throws SQLException {
    ArrayList<Vehicle> list = new ArrayList<>();
    try (Connection conn = getConnection()) {
      String sqlB = """
                SELECT v.*, b.bikeType
                  FROM vehicle v
                  JOIN bike b ON v.id = b.vehicleId
                 WHERE v.state = ?
                """;
      try (PreparedStatement ps = conn.prepareStatement(sqlB)) {
        ps.setString(1, state);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            list.add(new Bike(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("condition"),
                rs.getString("color"),
                rs.getDouble("pricePerDay"),
                rs.getString("bikeType"),
                rs.getString("ownerEmail"),
                rs.getString("state")
            ));
          }
        }
      }

      String sqlE = """
                SELECT v.*, e.bikeType, e.maxSpeed, e.oneChargeRange
                  FROM vehicle v
                  JOIN ebike e ON v.id = e.vehicleId
                 WHERE v.state = ?
                """;
      try (PreparedStatement ps = conn.prepareStatement(sqlE)) {
        ps.setString(1, state);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            list.add(new EBike(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("condition"),
                rs.getString("color"),
                rs.getDouble("pricePerDay"),
                rs.getString("bikeType"),
                rs.getInt("maxSpeed"),
                rs.getInt("oneChargeRange"),
                rs.getString("ownerEmail"),
                rs.getString("state")
            ));
          }
        }
      }

      String sqlS = """
                SELECT v.*, s.maxSpeed, s.oneChargeRange
                  FROM vehicle v
                  JOIN scooter s ON v.id = s.vehicleId
                 WHERE v.state = ?
                """;
      try (PreparedStatement ps = conn.prepareStatement(sqlS)) {
        ps.setString(1, state);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            list.add(new Scooter(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("condition"),
                rs.getString("color"),
                rs.getDouble("pricePerDay"),
                rs.getInt("maxSpeed"),
                rs.getInt("oneChargeRange"),
                rs.getString("ownerEmail"),
                rs.getString("state")
            ));
          }
        }
      }
    }
    return list;
  }

  @Override
  public ArrayList<Vehicle> getAll() throws SQLException {
    ArrayList<Vehicle> all = new ArrayList<>();
    all.addAll(getBike());
    all.addAll(getEBike());
    all.addAll(getScooter());
    return all;
  }

  @Override
  public Vehicle getByIdAndType(int id, String vehicleType) throws SQLException {
    try (Connection conn = getConnection()) {
      String sql;
      switch (vehicleType) {
        case "bike" ->
            sql = "SELECT v.*, b.bikeType FROM vehicle v JOIN bike b ON v.id=b.vehicleId WHERE v.id=?";
        case "e-bike" ->
            sql = "SELECT v.*, e.bikeType, e.maxSpeed, e.oneChargeRange FROM vehicle v JOIN ebike e ON v.id=e.vehicleId WHERE v.id=?";
        case "scooter" ->
            sql = "SELECT v.*, s.maxSpeed, s.oneChargeRange FROM vehicle v JOIN scooter s ON v.id=s.vehicleId WHERE v.id=?";
        default -> throw new SQLException("Unknown type: " + vehicleType);
      }
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return switch (vehicleType) {
              case "bike" -> new Bike(
                  id, rs.getString("type"), rs.getString("brand"),
                  rs.getString("model"), rs.getString("condition"),
                  rs.getString("color"), rs.getDouble("pricePerDay"),
                  rs.getString("bikeType"), rs.getString("ownerEmail"),
                  rs.getString("state")
              );
              case "e-bike" -> new EBike(
                  id, rs.getString("type"), rs.getString("brand"),
                  rs.getString("model"), rs.getString("condition"),
                  rs.getString("color"), rs.getDouble("pricePerDay"),
                  rs.getString("bikeType"), rs.getInt("maxSpeed"),
                  rs.getInt("oneChargeRange"), rs.getString("ownerEmail"),
                  rs.getString("state")
              );
              case "scooter" -> new Scooter(
                  id, rs.getString("type"), rs.getString("brand"),
                  rs.getString("model"), rs.getString("condition"),
                  rs.getString("color"), rs.getDouble("pricePerDay"),
                  rs.getInt("maxSpeed"), rs.getInt("oneChargeRange"),
                  rs.getString("ownerEmail"), rs.getString("state")
              );
              default -> null;
            };
          }
        }
      }
    }
    return null;
  }

  @Override
  public void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException {
    String updV = """
            UPDATE vehicle
               SET type=?, brand=?, model=?, condition=?, color=?, pricePerDay=?, ownerEmail=?, state=?
             WHERE id=?
            """;
    try (Connection conn = getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(updV)) {
        ps.setString(1, vehicle.getType());
        ps.setString(2, vehicle.getBrand());
        ps.setString(3, vehicle.getModel());
        ps.setString(4, vehicle.getCondition());
        ps.setString(5, vehicle.getColor());
        ps.setDouble(6, vehicle.getPricePerDay());
        ps.setString(7, vehicle.getOwnerEmail());
        ps.setString(8, vehicle.getState());
        ps.setInt(9, oldVehicle.getId());
        ps.executeUpdate();
      }

      switch (vehicle.getType()) {
        case "bike" -> {
          try (PreparedStatement ps = conn.prepareStatement(
              "UPDATE bike SET bikeType=? WHERE vehicleId=?")) {
            ps.setString(1, ((Bike)vehicle).getBikeType());
            ps.setInt(2, vehicle.getId());
            ps.executeUpdate();
          }
        }
        case "e-bike" -> {
          try (PreparedStatement ps = conn.prepareStatement(
              "UPDATE ebike SET bikeType=?, maxSpeed=?, oneChargeRange=? WHERE vehicleId=?")) {
            EBike eb = (EBike)vehicle;
            ps.setString(1, eb.getBikeType());
            ps.setInt(2, eb.getMaxSpeed());
            ps.setInt(3, eb.getOneChargeRange());
            ps.setInt(4, vehicle.getId());
            ps.executeUpdate();
          }
        }
        case "scooter" -> {
          try (PreparedStatement ps = conn.prepareStatement(
              "UPDATE scooter SET maxSpeed=?, oneChargeRange=? WHERE vehicleId=?")) {
            Scooter sc = (Scooter)vehicle;
            ps.setInt(1, sc.getMaxSpeed());
            ps.setInt(2, sc.getOneChargeRange());
            ps.setInt(3, vehicle.getId());
            ps.executeUpdate();
          }
        }
      }

      conn.commit();
    }
  }

  @Override
  public ArrayList<Vehicle> getByOwnerEmail(String ownerEmail) throws SQLException {
    ArrayList<Vehicle> list = new ArrayList<>();
    try (Connection conn = getConnection()) {
      String sqlB = """
            SELECT v.*, b.bikeType
              FROM vehicle v
              JOIN bike b ON v.id = b.vehicleId
             WHERE v.ownerEmail = ?
            """;
      try (PreparedStatement ps = conn.prepareStatement(sqlB)) {
        ps.setString(1, ownerEmail);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            list.add(new Bike(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("condition"),
                rs.getString("color"),
                rs.getDouble("pricePerDay"),
                rs.getString("bikeType"),
                rs.getString("ownerEmail"),
                rs.getString("state")
            ));
          }
        }
      }

      String sqlE = """
            SELECT v.*, e.bikeType, e.maxSpeed, e.oneChargeRange
              FROM vehicle v
              JOIN ebike e ON v.id = e.vehicleId
             WHERE v.ownerEmail = ?
            """;
      try (PreparedStatement ps = conn.prepareStatement(sqlE)) {
        ps.setString(1, ownerEmail);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            list.add(new EBike(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("condition"),
                rs.getString("color"),
                rs.getDouble("pricePerDay"),
                rs.getString("bikeType"),
                rs.getInt("maxSpeed"),
                rs.getInt("oneChargeRange"),
                rs.getString("ownerEmail"),
                rs.getString("state")
            ));
          }
        }
      }

      String sqlS = """
            SELECT v.*, s.maxSpeed, s.oneChargeRange
              FROM vehicle v
              JOIN scooter s ON v.id = s.vehicleId
             WHERE v.ownerEmail = ?
            """;
      try (PreparedStatement ps = conn.prepareStatement(sqlS)) {
        ps.setString(1, ownerEmail);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            list.add(new Scooter(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("condition"),
                rs.getString("color"),
                rs.getDouble("pricePerDay"),
                rs.getInt("maxSpeed"),
                rs.getInt("oneChargeRange"),
                rs.getString("ownerEmail"),
                rs.getString("state")
            ));
          }
        }
      }
    }
    return list;
  }


  @Override
  public void delete(Vehicle vehicle) throws SQLException {
    try (Connection conn = getConnection()) {
      conn.setAutoCommit(false);

      String delSub = switch (vehicle.getType()) {
        case "bike"    -> "DELETE FROM bike WHERE vehicleId=?";
        case "e-bike"  -> "DELETE FROM ebike WHERE vehicleId=?";
        case "scooter" -> "DELETE FROM scooter WHERE vehicleId=?";
        default        -> throw new SQLException("Unknown type");
      };
      try (PreparedStatement ps = conn.prepareStatement(delSub)) {
        ps.setInt(1, vehicle.getId());
        ps.executeUpdate();
      }

      try (PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM vehicle WHERE id=?")) {
        ps.setInt(1, vehicle.getId());
        ps.executeUpdate();
      }

      conn.commit();
    }
  }

  @Override
  public void deleteAll(String ownerEmail) throws SQLException {
    try (Connection conn = getConnection()) {
      conn.setAutoCommit(false);

      for (String sub : new String[]{"bike","ebike","scooter"}) {
        String sql = String.format(
            "DELETE FROM %s WHERE vehicleId IN (SELECT id FROM vehicle WHERE ownerEmail=?)",
            sub
        );
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
          ps.setString(1, ownerEmail);
          ps.executeUpdate();
        }
      }

      try (PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM vehicle WHERE ownerEmail=?")) {
        ps.setString(1, ownerEmail);
        ps.executeUpdate();
      }

      conn.commit();
    }
  }

  private ArrayList<Vehicle> getBike() throws SQLException {
    var list = new ArrayList<Vehicle>();
    String sql = """
            SELECT v.*, b.bikeType
              FROM vehicle v
              JOIN bike b ON v.id = b.vehicleId
            """;
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        list.add(new Bike(
            rs.getInt("id"),
            rs.getString("type"),
            rs.getString("brand"),
            rs.getString("model"),
            rs.getString("condition"),
            rs.getString("color"),
            rs.getDouble("pricePerDay"),
            rs.getString("bikeType"),
            rs.getString("ownerEmail"),
            rs.getString("state")
        ));
      }
    }
    return list;
  }

  private ArrayList<Vehicle> getEBike() throws SQLException {
    var list = new ArrayList<Vehicle>();
    String sql = """
            SELECT v.*, e.bikeType, e.maxSpeed, e.oneChargeRange
              FROM vehicle v
              JOIN ebike e ON v.id = e.vehicleId
            """;
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        list.add(new EBike(
            rs.getInt("id"),
            rs.getString("type"),
            rs.getString("brand"),
            rs.getString("model"),
            rs.getString("condition"),
            rs.getString("color"),
            rs.getDouble("pricePerDay"),
            rs.getString("bikeType"),
            rs.getInt("maxSpeed"),
            rs.getInt("oneChargeRange"),
            rs.getString("ownerEmail"),
            rs.getString("state")
        ));
      }
    }
    return list;
  }

  private ArrayList<Vehicle> getScooter() throws SQLException {
    var list = new ArrayList<Vehicle>();
    String sql = """
            SELECT v.*, s.maxSpeed, s.oneChargeRange
              FROM vehicle v
              JOIN scooter s ON v.id = s.vehicleId
            """;
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        list.add(new Scooter(
            rs.getInt("id"),
            rs.getString("type"),
            rs.getString("brand"),
            rs.getString("model"),
            rs.getString("condition"),
            rs.getString("color"),
            rs.getDouble("pricePerDay"),
            rs.getInt("maxSpeed"),
            rs.getInt("oneChargeRange"),
            rs.getString("ownerEmail"),
            rs.getString("state")
        ));
      }
    }
    return list;
  }
}
