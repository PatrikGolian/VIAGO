package persistence.vehicle;

import model.entities.vehicles.Vehicle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface VehicleDao
{
  void add(Vehicle vehicle) throws SQLException;
  Vehicle getByIdAndType(int id, String vehicleType) throws SQLException;
  ArrayList<Vehicle> getByType(String type) throws SQLException;
  ArrayList<Vehicle> getByState(String state) throws SQLException;
  ArrayList<Vehicle> getByOwnerEmail(String ownerEmail) throws SQLException;
  ArrayList<Vehicle> getAll() throws SQLException;
  void delete(Vehicle vehicle) throws SQLException;
  void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException;
}
