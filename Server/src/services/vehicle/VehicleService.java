package services.vehicle;

import dtos.reservation.ReservationDto;
import dtos.vehicle.AddNewVehicleRequest;
import dtos.vehicle.VehicleDisplayDto;

import java.sql.SQLException;
import java.util.List;

public interface VehicleService
{
  void addNew(AddNewVehicleRequest request) throws SQLException;
  List<VehicleDisplayDto> getVehiclesOverview() throws SQLException;
}
