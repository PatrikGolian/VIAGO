package services.allvehicles;

import dtos.vehicle.DeleteVehicleRequest;
import dtos.vehicle.VehicleDisplayDto;

import java.sql.SQLException;
import java.util.List;

public interface AllVehiclesService
{
  List<VehicleDisplayDto> getVehiclesOverview() throws SQLException;
  void delete(DeleteVehicleRequest request);
}
