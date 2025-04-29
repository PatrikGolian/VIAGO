package services.vehicle;

import dtos.vehicle.AddNewVehicleRequest;
import model.entities.vehicles.Vehicle;
import persistence.daos.vehicle.VehicleDao;

import java.sql.SQLException;

public interface  VehicleService
{
  void addNew(AddNewVehicleRequest request) throws SQLException;

}
