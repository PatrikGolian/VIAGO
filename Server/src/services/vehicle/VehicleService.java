package services.vehicle;

import dtos.Request;
import dtos.user.ViewUsers;
import dtos.vehicle.AddNewVehicleRequest;
import dtos.vehicle.VehicleDisplayDto;
import model.entities.vehicles.Vehicle;
import persistence.daos.vehicle.VehicleDao;

import java.sql.SQLException;
import java.util.List;

public interface  VehicleService
{
  void addNew(AddNewVehicleRequest request) throws SQLException;
  List<VehicleDisplayDto> getVehiclesOverview()
      throws SQLException;
}
