package services.myvehicles;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationReserveRequest;
import dtos.vehicle.VehicleDataDto;
import dtos.vehicle.VehicleDisplayDto;
import dtos.vehicle.VehicleOwnerRequest;

import java.sql.SQLException;
import java.util.List;

public interface MyVehiclesService
{
  List<VehicleDisplayDto> getVehiclesOverview(
      VehicleOwnerRequest request)
      throws SQLException;
  void delete();
}
