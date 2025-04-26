package persistence.daos.vehicle;

import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    String type = vehicle.getType(), brand = vehicle.getBrand(), model = vehicle.getModel(),
        condition = vehicle.getCondition(), color = vehicle.getColor(), biketype =null;
    double pricePerDay = vehicle.getPricePerDay();

    int typeInt;
    if (!type.equals("bike"))
    {
      if (!type.equals("e-bike"))
      {
        typeInt = 3;
      }
      else
      {
        typeInt = 2;
      }
    }
    else
    {
      typeInt = 1;
    }


    try (Connection connection = getConnection())
    {
      PreparedStatement statement =
          connection.prepareStatement("INSERT INTO vehicle(id, type, brand, model, condition, color, pricePerDay, bikeType, maxSpeed, oneChargeRange) VALUES(?,?,?,?,?,?,?,?,?,?)");
      statement.setInt(1,id);
      statement.setString(2,type);
      statement.setString(3,brand);
      statement.setString(4,model);
      statement.setString(5,condition);
      statement.setString(6,color);
      statement.setDouble(7,pricePerDay);
      switch (typeInt)
      {
        case 1:
          biketype = ((Bike) vehicle).getBikeType();
          statement.setString(8,biketype);
          break;
        case 2:
          maxSpeed = ((EBike) vehicle).getMaxSpeed();
          oneChargeRange = ((EBike) vehicle).getOneChargeRange();
          biketype = ((EBike) vehicle).getBikeType();
          statement.setString(8,biketype);
          statement.setInt(9,maxSpeed);
          statement.setInt(10,oneChargeRange);
          break;
        case 3:
          maxSpeed = ((Scooter) vehicle).getMaxSpeed();
          oneChargeRange = ((Scooter) vehicle).getOneChargeRange();
          statement.setInt(9,maxSpeed);
          statement.setInt(10,oneChargeRange);
          break;
      }
      statement.executeUpdate();

      return vehicle;
    }

  }

  @Override public void add(Vehicle vehicle) throws SQLException
  {
    create(vehicle);
  }

  @Override public ArrayList<Vehicle> getByType(String type) throws SQLException
  {
    ArrayList<Vehicle> vehicles = null;
    try(Connection connection = getConnection()){
      PreparedStatement statement = connection.prepareStatement("SELECT* FROM vehicle WHERE type =?");
      statement.setString(1,type);
      ResultSet resultSet = statement.executeQuery();
      if(resultSet.next())
      {
        int id = resultSet.getInt("id"),maxSpeed,oneChargeRange;
        String brand = resultSet.getString("brand"),
        model = resultSet.getString("model"),
        condition = resultSet.getString("condition"),
        color = resultSet.getString("color"), bikeType;
        double pricePerDay = resultSet.getDouble("pricePerDay");

        int typeInt = 0;
        if (type.equals("bike")){
          typeInt = 1;
        }
        else if (type.equals("e-bike"))
        {
          typeInt = 2;
        }
        else if (type.equals("scooter"))
        {
          typeInt = 3;
        }
        switch (typeInt)
        {
          case 1:
            bikeType = resultSet.getString("bikeType");
            Bike bike = new Bike(type,brand,model,condition,color,pricePerDay,bikeType);
            bike.setId(id);
            vehicles.add(bike);
            break;
          case 2:
            bikeType = resultSet.getString("biketype");
            maxSpeed = resultSet.getInt("maxSpeed");
            oneChargeRange = resultSet.getInt("oneChargeRange");
            EBike eBike = new EBike(type,brand,model,condition,color,pricePerDay,maxSpeed,oneChargeRange, bikeType);
            eBike.setId(id);
            vehicles.add(eBike);
            break;
          case 3:
            maxSpeed = resultSet.getInt("maxSpeed");
            oneChargeRange = resultSet.getInt("oneChargeRange");
            Scooter scooter = new Scooter(type,brand,model,condition,color,pricePerDay,maxSpeed,oneChargeRange);
            scooter.setId(id);
            vehicles.add(scooter);
            break;
          default:
            System.out.println("Problem with VehiclePostgresDao initializing vehicle type");
        }

      }
      return  vehicles;
    }
  }

  @Override public void delete(Vehicle vehicle) throws SQLException
  {
    try(Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM account WHERE id = ?");
      statement.setInt(1, vehicle.getId());
      statement.executeUpdate();
    }
  }

  @Override public void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException
  {
    try(Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE vehicle SET type = ?, brand = ?, model = ?, condition = ?, color = ?, pricePerDay = ?, bikeType = ?, maxSpeed = ?, oneChargeRange = ? WHERE id = ?");
      statement.setString(1, vehicle.getType());
      statement.setString(2, vehicle.getBrand());
      statement.setString(3, vehicle.getModel());
      statement.setString(4, vehicle.getCondition());
      statement.setString(5, vehicle.getColor());
      statement.setDouble(6, vehicle.getPricePerDay());
      int typeInt;
      String type = vehicle.getType();
      if (!type.equals("bike"))
      {
        if (!type.equals("e-bike"))
        {
          typeInt = 3;
        }
        else
        {
          typeInt = 2;
        }
      }
      else
      {
        typeInt = 1;
      }
      switch (typeInt)
      {
        case 1:
          statement.setString(7, ((Bike) vehicle).getBikeType());
          break;
        case 2:
          statement.setInt(8,((EBike) vehicle).getMaxSpeed());
          statement.setInt(9,((EBike) vehicle).getOneChargeRange());
          break;
        case 3:
          statement.setInt(8,((Scooter) vehicle).getMaxSpeed());
          statement.setInt(9,((Scooter) vehicle).getOneChargeRange());
          break;
      }
      statement.executeUpdate();
    }
  }

}