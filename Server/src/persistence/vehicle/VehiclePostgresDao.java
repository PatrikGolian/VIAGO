package persistence.vehicle;

import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;

import java.sql.*;
import java.util.ArrayList;

public class VehiclePostgresDao implements VehicleDao
{
  private static VehiclePostgresDao instance;

  private VehiclePostgresDao() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  private static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=viago",
        "postgres", "password");
  }

  public static synchronized VehiclePostgresDao getInstance()
      throws SQLException
  {
    if (instance == null)
    {
      instance = new VehiclePostgresDao();
    }
    return instance;
  }

  public Vehicle create(Vehicle vehicle) throws SQLException
  {
    int id = vehicle.getId(), maxSpeed, oneChargeRange;
    String type = vehicle.getType(), brand = vehicle.getBrand(), model = vehicle.getModel(), condition = vehicle.getCondition(), color = vehicle.getColor(), biketype = null, ownerEmail = vehicle.getOwnerEmail(), state = vehicle.getState();
    double pricePerDay = vehicle.getPricePerDay();
    ResultSet keys;

    try (Connection connection = getConnection())
    {
      PreparedStatement statement;
      switch (type)
      {
        case "bike":
          statement = connection.prepareStatement(
              "INSERT INTO bike(type, brand, model, condition, color, pricePerDay, bikeType, ownerEmail, state) VALUES(?,?,?,?,?,?,?,?,?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
          statement.setString(1, type);
          statement.setString(2, brand);
          statement.setString(3, model);
          statement.setString(4, condition);
          statement.setString(5, color);
          statement.setDouble(6, pricePerDay);
          biketype = ((Bike) vehicle).getBikeType();
          statement.setString(7, biketype);
          statement.setString(8, ownerEmail);
          statement.setString(9, state);
          statement.executeUpdate();
          keys = statement.getGeneratedKeys();
          if (keys.next())
          {
            return (Vehicle) (new Bike(keys.getInt(1), type, brand, model,
                condition, color, pricePerDay, biketype, ownerEmail, state));
          }
          else
          {
            throw new SQLException("No keys generated");
          }
        case "e-bike":
          statement = connection.prepareStatement(
              "INSERT INTO eBike(type, brand, model, condition, color, pricePerDay, bikeType, maxSpeed, oneChargeRange, ownerEmail, state) VALUES(?,?,?,?,?,?,?,?,?,?,?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
          statement.setString(1, type);
          statement.setString(2, brand);
          statement.setString(3, model);
          statement.setString(4, condition);
          statement.setString(5, color);
          statement.setDouble(6, pricePerDay);
          maxSpeed = ((EBike) vehicle).getMaxSpeed();
          oneChargeRange = ((EBike) vehicle).getOneChargeRange();
          biketype = ((EBike) vehicle).getBikeType();
          statement.setString(7, biketype);
          statement.setInt(8, maxSpeed);
          statement.setInt(9, oneChargeRange);
          statement.setString(10, ownerEmail);
          statement.setString(11, state);
          statement.executeUpdate();
          keys = statement.getGeneratedKeys();
          if (keys.next())
          {
            return (Vehicle) (new EBike(keys.getInt(1), type, brand, model,
                condition, color, pricePerDay, biketype, maxSpeed,
                oneChargeRange, ownerEmail, state));
          }
          else
          {
            throw new SQLException("No keys generated");
          }
        case "scooter":
          statement = connection.prepareStatement(
              "INSERT INTO scooter(type, brand, model, condition, color, pricePerDay, maxSpeed, oneChargeRange, ownerEmail, state) VALUES(?,?,?,?,?,?,?,?,?,?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
          statement.setString(1, type);
          statement.setString(2, brand);
          statement.setString(3, model);
          statement.setString(4, condition);
          statement.setString(5, color);
          statement.setDouble(6, pricePerDay);
          maxSpeed = ((Scooter) vehicle).getMaxSpeed();
          oneChargeRange = ((Scooter) vehicle).getOneChargeRange();
          statement.setInt(7, maxSpeed);
          statement.setInt(8, oneChargeRange);
          statement.setString(9, ownerEmail);
          statement.setString(10, state);
          statement.executeUpdate();
          keys = statement.getGeneratedKeys();
          if (keys.next())
          {
            return (Vehicle) (new Scooter(keys.getInt(1), type, brand, model,
                condition, color, pricePerDay, maxSpeed, oneChargeRange,
                ownerEmail, state));
          }
          else
          {
            throw new SQLException("No keys generated");
          }
        default:
          throw new IllegalStateException("Unexpected value: " + type);
      }
    }
  }

  @Override public void add(Vehicle vehicle) throws SQLException
  {
    create(vehicle);
  }

  @Override public ArrayList<Vehicle> getByType(String type) throws SQLException
  {
    ArrayList<Vehicle> vehicles = null;
    try (Connection connection = getConnection())
    {
      switch (type)
      {

        case "bike":
          vehicles = getBike(connection);
          break;
        case "e-bike":
          vehicles = getEBike(connection);
          break;
        case "scooter":
          vehicles = getScooter(connection);
          break;

      }
    }
    return vehicles;
  }

  @Override public ArrayList<Vehicle> getByState(String state)
      throws SQLException
  {
    ArrayList<Vehicle> vehicles = null;
    try (Connection connection = getConnection())
    {
      //for bike
      PreparedStatement statement = connection.prepareStatement(
          "SELECT* FROM bike where state = ?");
      statement.setString(1, state);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        int id = resultSet.getInt("id"), maxSpeed, oneChargeRange;
        String brand = resultSet.getString(
            "brand"), model = resultSet.getString(
            "model"), condition = resultSet.getString(
            "condition"), color = resultSet.getString(
            "color"), bikeType, ownerEmail = resultSet.getString(
            "ownerEmail"), type = resultSet.getString("type");
        double pricePerDay = resultSet.getDouble("pricePerDay");
        bikeType = resultSet.getString("bikeType");
        Bike bike = new Bike(id, type, brand, model, condition, color,
            pricePerDay, bikeType, ownerEmail, state);
        bike.setId(id);
        vehicles.add(bike);
      }
      //for eBike
      statement = connection.prepareStatement(
          "SELECT* FROM eBike where state = ?");
      statement.setString(1, state);
      resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        int id = resultSet.getInt("id"), maxSpeed, oneChargeRange;
        String brand = resultSet.getString(
            "brand"), model = resultSet.getString(
            "model"), condition = resultSet.getString(
            "condition"), color = resultSet.getString(
            "color"), bikeType = resultSet.getString(
            "biketype"), ownerEmail = resultSet.getString(
            "ownerEmail"), type = resultSet.getString("type");
        double pricePerDay = resultSet.getDouble("pricePerDay");
        maxSpeed = resultSet.getInt("maxSpeed");
        oneChargeRange = resultSet.getInt("oneChargeRange");
        EBike eBike = new EBike(id, type, brand, model, condition, color,
            pricePerDay, bikeType, maxSpeed, oneChargeRange, ownerEmail, state);
        eBike.setId(id);
        vehicles.add(eBike);
      }
      //for Scooter
      statement = connection.prepareStatement(
          "SELECT* FROM scooter where state = ?");
      statement.setString(1, state);
      resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        int id = resultSet.getInt("id"), maxSpeed = resultSet.getInt(
            "maxSpeed"), oneChargeRange = resultSet.getInt("oneChargeRange");
        ;
        String brand = resultSet.getString(
            "brand"), model = resultSet.getString(
            "model"), condition = resultSet.getString(
            "condition"), color = resultSet.getString(
            "color"), ownerEmail = resultSet.getString(
            "ownerEmail"), type = resultSet.getString("type");
        double pricePerDay = resultSet.getDouble("pricePerDay");
        Scooter scooter = new Scooter(id, type, brand, model, condition, color,
            pricePerDay, maxSpeed, oneChargeRange, ownerEmail, state);
        scooter.setId(id);
        vehicles.add(scooter);
      }
    }
    return vehicles;
  }

  @Override public ArrayList<Vehicle> getAll() throws SQLException
  {
    ArrayList<Vehicle> vehicles = new ArrayList<>();

    try (Connection connection = getConnection())
    {
      ArrayList<Vehicle> bikes = getBike(connection);
      ArrayList<Vehicle> eBikes = getEBike(connection);
      ArrayList<Vehicle> scooters = getScooter(connection);
      for (int b = 0; b < bikes.size(); b++)
      {
        vehicles.add(bikes.get(b));
      }
      for (int e = 0; e < eBikes.size(); e++)
      {
        vehicles.add(eBikes.get(e));
      }
      for (int s = 0; s < scooters.size(); s++)
      {
        vehicles.add(scooters.get(s));
      }
    }
    return vehicles;
  }

  private ArrayList<Vehicle> getBike(Connection connection) throws SQLException
  {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    PreparedStatement statement = connection.prepareStatement(
        "SELECT* FROM bike");
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next())
    {
      int id = resultSet.getInt("id"), maxSpeed, oneChargeRange;
      String brand = resultSet.getString("brand"), model = resultSet.getString(
          "model"), condition = resultSet.getString(
          "condition"), color = resultSet.getString(
          "color"), bikeType, ownerEmail = resultSet.getString(
          "ownerEmail"), state = resultSet.getString(
          "state"), type = resultSet.getString("type");
      double pricePerDay = resultSet.getDouble("pricePerDay");
      bikeType = resultSet.getString("bikeType");
      Bike bike = new Bike(id, type, brand, model, condition, color,
          pricePerDay, bikeType, ownerEmail, state);
      bike.setId(id);
      vehicles.add(bike);
    }
    return vehicles;
  }

  private ArrayList<Vehicle> getEBike(Connection connection) throws SQLException
  {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    PreparedStatement statement = connection.prepareStatement(
        "SELECT* FROM eBike");
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next())
    {
      int id = resultSet.getInt("id"), maxSpeed, oneChargeRange;
      String brand = resultSet.getString("brand"), model = resultSet.getString(
          "model"), condition = resultSet.getString(
          "condition"), color = resultSet.getString(
          "color"), bikeType = resultSet.getString(
          "biketype"), ownerEmail = resultSet.getString(
          "ownerEmail"), state = resultSet.getString(
          "state"), type = resultSet.getString("type");
      double pricePerDay = resultSet.getDouble("pricePerDay");
      maxSpeed = resultSet.getInt("maxSpeed");
      oneChargeRange = resultSet.getInt("oneChargeRange");
      EBike eBike = new EBike(id, type, brand, model, condition, color,
          pricePerDay, bikeType, maxSpeed, oneChargeRange, ownerEmail, state);
      eBike.setId(id);
      vehicles.add(eBike);
    }
    return vehicles;
  }

  private ArrayList<Vehicle> getScooter(Connection connection)
      throws SQLException
  {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    PreparedStatement statement = connection.prepareStatement(
        "SELECT* FROM scooter");
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next())
    {
      int id = resultSet.getInt("id"), maxSpeed = resultSet.getInt(
          "maxSpeed"), oneChargeRange = resultSet.getInt("oneChargeRange");
      ;
      String brand = resultSet.getString("brand"), model = resultSet.getString(
          "model"), condition = resultSet.getString(
          "condition"), color = resultSet.getString(
          "color"), ownerEmail = resultSet.getString(
          "ownerEmail"), state = resultSet.getString(
          "state"), type = resultSet.getString("type");
      double pricePerDay = resultSet.getDouble("pricePerDay");
      Scooter scooter = new Scooter(id, type, brand, model, condition, color,
          pricePerDay, maxSpeed, oneChargeRange, ownerEmail, state);
      scooter.setId(id);
      vehicles.add(scooter);
    }
    return vehicles;
  }

  @Override public void delete(Vehicle vehicle) throws SQLException
  {
    String type = vehicle.getType();
    PreparedStatement statement;
    try (Connection connection = getConnection())
    {
      switch (type)
      {
        case "bike":
          statement = connection.prepareStatement(
              "DELETE FROM bike WHERE id = ?");
          statement.setInt(1, vehicle.getId());
          statement.executeUpdate();
          break;
        case "e-bike":
          statement = connection.prepareStatement(
              "DELETE FROM eBike WHERE id = ?");
          statement.setInt(1, vehicle.getId());
          statement.executeUpdate();
          break;
        case "scooter":
          statement = connection.prepareStatement(
              "DELETE FROM scooter WHERE id = ?");
          statement.setInt(1, vehicle.getId());
          statement.executeUpdate();
          break;
      }
    }
  }

  @Override public void save(Vehicle vehicle, Vehicle oldVehicle)
      throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement;
      String type = vehicle.getType();

      switch (type)
      {
        case "bike":
          statement = connection.prepareStatement(
              "UPDATE bike SET type = ?, brand = ?, model = ?, condition = ?, color = ?, pricePerDay = ?, bikeType = ?,ownerEmail = ?, state = ? WHERE id = ?");
          statement.setString(1, vehicle.getType());
          statement.setString(2, vehicle.getBrand());
          statement.setString(3, vehicle.getModel());
          statement.setString(4, vehicle.getCondition());
          statement.setString(5, vehicle.getColor());
          statement.setDouble(6, vehicle.getPricePerDay());
          statement.setString(7, ((Bike) vehicle).getBikeType());
          statement.setString(8, vehicle.getOwnerEmail());
          statement.setString(9, vehicle.getState());
          statement.setInt(10, oldVehicle.getId());
          break;
        case "e-bike":
          statement = connection.prepareStatement(
              "UPDATE eBike SET type = ?, brand = ?, model = ?, condition = ?, color = ?, pricePerDay = ?, bikeType = ?, maxSpeed = ?, oneChargeRange = ?,ownerEmail = ?, state = ? WHERE id = ?");
          statement.setString(1, vehicle.getType());
          statement.setString(2, vehicle.getBrand());
          statement.setString(3, vehicle.getModel());
          statement.setString(4, vehicle.getCondition());
          statement.setString(5, vehicle.getColor());
          statement.setDouble(6, vehicle.getPricePerDay());
          statement.setString(7, ((EBike) vehicle).getBikeType());
          statement.setInt(8, ((EBike) vehicle).getMaxSpeed());
          statement.setInt(9, ((EBike) vehicle).getOneChargeRange());
          statement.setString(10, vehicle.getOwnerEmail());
          statement.setString(11, vehicle.getState());
          statement.setInt(12, oldVehicle.getId());
          break;
        case "scooter":
          statement = connection.prepareStatement(
              "UPDATE scooter SET type = ?, brand = ?, model = ?, condition = ?, color = ?, pricePerDay = ?, maxSpeed = ?, oneChargeRange = ?, ownerEmail = ?, state = ? WHERE id = ?");
          statement.setString(1, vehicle.getType());
          statement.setString(2, vehicle.getBrand());
          statement.setString(3, vehicle.getModel());
          statement.setString(4, vehicle.getCondition());
          statement.setString(5, vehicle.getColor());
          statement.setDouble(6, vehicle.getPricePerDay());
          statement.setInt(7, ((Scooter) vehicle).getMaxSpeed());
          statement.setInt(8, ((Scooter) vehicle).getOneChargeRange());
          statement.setString(9, vehicle.getOwnerEmail());
          statement.setString(10, vehicle.getState());
          statement.setInt(11, oldVehicle.getId());
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + type);
      }
      statement.executeUpdate();
    }
  }

  @Override public Vehicle getByIdAndType(int id, String vehicleType)
      throws SQLException
  {
    ArrayList<Vehicle> temp = new ArrayList<>();
    try (Connection connection = getConnection())
    {
      switch (vehicleType)
      {
        case "bike":
          temp = getBike(connection);
          break;
        case "e-bike":
          temp = getEBike(connection);
          break;
        case "scooter":
          temp = getScooter(connection);
          break;
      }
    }
    for (int t = 0; t < temp.size(); t++)
    {
      if (temp.get(t).getId() == id)
      {
        return temp.get(t);
      }
    }
    return null;
  }
}
