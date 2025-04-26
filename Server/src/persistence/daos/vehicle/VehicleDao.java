package persistence.daos.vehicle;

import model.entities.vehicles.Vehicle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface VehicleDao
{
  void add(Vehicle vehicle) throws SQLException;
  ArrayList<Vehicle> getByType(String type) throws SQLException;
  void delete(Vehicle vehicle) throws SQLException;
  void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException;
}
