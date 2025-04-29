package persistence.daos.vehicle;

import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;

import java.sql.*;
import java.util.ArrayList;

public class VehiclePostgresDao implements VehicleDao
{
  private static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=viago",
        "postgres", "password");
  }

  public Vehicle create(Vehicle vehicle) throws SQLException
  {
    int id = vehicle.getId(), maxSpeed, oneChargeRange;
    String type = vehicle.getType(), brand = vehicle.getBrand(), model = vehicle.getModel(), condition = vehicle.getCondition(), color = vehicle.getColor(), biketype = null;
    double pricePerDay = vehicle.getPricePerDay();
    ResultSet keys;

    try (Connection connection = getConnection())
    {
      PreparedStatement statement;
      switch (type)
      {
        case "bike":
          statement = connection.prepareStatement(
              "INSERT INTO bike(type, brand, model, condition, color, pricePerDay, bikeType) VALUES(?,?,?,?,?,?,?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
          statement.setString(1, type);
          statement.setString(2, brand);
          statement.setString(3, model);
          statement.setString(4, condition);
          statement.setString(5, color);
          statement.setDouble(6, pricePerDay);
          biketype = ((Bike) vehicle).getBikeType();
          statement.setString(7, biketype);
          statement.executeUpdate();
          keys = statement.getGeneratedKeys();
          if (keys.next())
          {
            return (Vehicle) (new Bike(keys.getInt(1), type, brand, model,
                condition, color, pricePerDay, biketype));
          }
          else
          {
            throw new SQLException("No keys generated");
          }
        case "e-bike":
          statement = connection.prepareStatement(
              "INSERT INTO eBike(type, brand, model, condition, color, pricePerDay, bikeType, maxSpeed, oneChargeRange) VALUES(?,?,?,?,?,?,?,?,?)",
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
          statement.executeUpdate();
          keys = statement.getGeneratedKeys();
          if (keys.next())
          {
            return (Vehicle) (new EBike(keys.getInt(1), type, brand, model,
                condition, color, pricePerDay, biketype, maxSpeed,
                oneChargeRange));
          }
          else
          {
            throw new SQLException("No keys generated");
          }
        case "scooter":
          statement = connection.prepareStatement(
              "INSERT INTO scooter(type, brand, model, condition, color, pricePerDay, maxSpeed, oneChargeRange) VALUES(?,?,?,?,?,?,?,?)",
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
          statement.executeUpdate();
          keys = statement.getGeneratedKeys();
          if (keys.next())
          {
            return (Vehicle) (new Scooter(keys.getInt(1), type, brand, model,
                condition, color, pricePerDay, maxSpeed, oneChargeRange));
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
          vehicles = getBike(connection, type);
          break;
        case "e-bike":
          vehicles = getEBike(connection, type);
          break;
        case "scooter":
          vehicles = getScooter(connection, type);
          break;

      }
    }
    return vehicles;
  }


  private ArrayList<Vehicle> getBike(Connection connection, String type) throws SQLException
  {
    ArrayList<Vehicle> vehicles = null;
    PreparedStatement statement = connection.prepareStatement("SELECT* FROM bike");
    ResultSet resultSet = statement.executeQuery();
    if(resultSet.next())
    {
      int id = resultSet.getInt("id"), maxSpeed, oneChargeRange;
      String brand = resultSet.getString(
          "brand"), model = resultSet.getString(
          "model"), condition = resultSet.getString(
          "condition"), color = resultSet.getString("color"), bikeType;
      double pricePerDay = resultSet.getDouble("pricePerDay");
      bikeType = resultSet.getString("bikeType");
      Bike bike = new Bike(id,type, brand, model, condition, color, pricePerDay,
          bikeType);
      bike.setId(id);
      vehicles.add(bike);
    }
    return vehicles;
  }
  private ArrayList<Vehicle> getEBike(Connection connection, String type) throws SQLException
  {
    ArrayList<Vehicle> vehicles = null;
    PreparedStatement statement = connection.prepareStatement("SELECT* FROM eBike");
    ResultSet resultSet = statement.executeQuery();
    if(resultSet.next())
    {
      int id = resultSet.getInt("id"), maxSpeed, oneChargeRange;
      String brand = resultSet.getString(
          "brand"), model = resultSet.getString(
          "model"), condition = resultSet.getString(
          "condition"), color = resultSet.getString("color"), bikeType;
      double pricePerDay = resultSet.getDouble("pricePerDay");
      bikeType = resultSet.getString("biketype");
      maxSpeed = resultSet.getInt("maxSpeed");
      oneChargeRange = resultSet.getInt("oneChargeRange");
      EBike eBike = new EBike(id,type, brand, model, condition, color,
          pricePerDay,bikeType, maxSpeed, oneChargeRange);
      eBike.setId(id);
      vehicles.add(eBike);
    }
    return vehicles;
  }
  private ArrayList<Vehicle> getScooter(Connection connection, String type)
      throws SQLException
  {
    ArrayList<Vehicle> vehicles = null;
    PreparedStatement statement = connection.prepareStatement("SELECT* FROM scooter");
    ResultSet resultSet = statement.executeQuery();
    if(resultSet.next())
    {
      int id = resultSet.getInt("id"), maxSpeed, oneChargeRange;
      String brand = resultSet.getString(
          "brand"), model = resultSet.getString(
          "model"), condition = resultSet.getString(
          "condition"), color = resultSet.getString("color"), bikeType;
      double pricePerDay = resultSet.getDouble("pricePerDay");

      maxSpeed = resultSet.getInt("maxSpeed");
      oneChargeRange = resultSet.getInt("oneChargeRange");
      Scooter scooter = new Scooter(id, type, brand, model, condition, color,
          pricePerDay, maxSpeed, oneChargeRange);
      scooter.setId(id);
      vehicles.add(scooter);
    }
    return vehicles;
  }

  @Override public void delete(Vehicle vehicle) throws SQLException
  {
    String type = vehicle.getType();
    PreparedStatement statement;
    try(Connection connection = getConnection())
    {
      switch (type)
      {
        case "bike":
          statement = connection.prepareStatement("DELETE FROM bike WHERE id = ?");
          statement.setInt(1, vehicle.getId());
          statement.executeUpdate();
          break;
        case "e-bike":
          statement = connection.prepareStatement("DELETE FROM eBike WHERE id = ?");
          statement.setInt(1, vehicle.getId());
          statement.executeUpdate();
          break;
        case "scooter":
          statement = connection.prepareStatement("DELETE FROM scooter WHERE id = ?");
          statement.setInt(1, vehicle.getId());
          statement.executeUpdate();
          break;
      }
    }
  }

  @Override public void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException
  {
    try(Connection connection = getConnection())
    {
      PreparedStatement statement;
      String type = vehicle.getType();

      switch (type)
      {
        case "bike":
          statement= connection.prepareStatement("UPDATE bike SET type = ?, brand = ?, model = ?, condition = ?, color = ?, pricePerDay = ?, bikeType = ? WHERE id = ?");
          statement.setString(1, vehicle.getType());
          statement.setString(2, vehicle.getBrand());
          statement.setString(3, vehicle.getModel());
          statement.setString(4, vehicle.getCondition());
          statement.setString(5, vehicle.getColor());
          statement.setDouble(6, vehicle.getPricePerDay());
          statement.setString(7, ((Bike) vehicle).getBikeType());
          statement.setInt(8,oldVehicle.getId());
          break;
        case "e-bike":
          statement= connection.prepareStatement("UPDATE eBike SET type = ?, brand = ?, model = ?, condition = ?, color = ?, pricePerDay = ?, bikeType = ?, maxSpeed = ?, oneChargeRange = ? WHERE id = ?");
          statement.setString(1, vehicle.getType());
          statement.setString(2, vehicle.getBrand());
          statement.setString(3, vehicle.getModel());
          statement.setString(4, vehicle.getCondition());
          statement.setString(5, vehicle.getColor());
          statement.setDouble(6, vehicle.getPricePerDay());
          statement.setString(7, ((EBike) vehicle).getBikeType());
          statement.setInt(8,((EBike) vehicle).getMaxSpeed());
          statement.setInt(9,((EBike) vehicle).getOneChargeRange());
          statement.setInt(10,oldVehicle.getId());
          break;
        case "scooter":
          statement= connection.prepareStatement("UPDATE scooter SET type = ?, brand = ?, model = ?, condition = ?, color = ?, pricePerDay = ?, maxSpeed = ?, oneChargeRange = ? WHERE id = ?");
          statement.setString(1, vehicle.getType());
          statement.setString(2, vehicle.getBrand());
          statement.setString(3, vehicle.getModel());
          statement.setString(4, vehicle.getCondition());
          statement.setString(5, vehicle.getColor());
          statement.setDouble(6, vehicle.getPricePerDay());
          statement.setInt(7,((Scooter) vehicle).getMaxSpeed());
          statement.setInt(8,((Scooter) vehicle).getOneChargeRange());
          statement.setInt(9,oldVehicle.getId());
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + type);
      }
      statement.executeUpdate();
    }
  }

}
