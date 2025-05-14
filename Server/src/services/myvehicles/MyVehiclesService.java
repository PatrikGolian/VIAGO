package services.myvehicles;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.vehicle.*;

import java.sql.SQLException;
import java.util.List;

public interface MyVehiclesService
{
  List<VehicleDisplayDto> getVehiclesOverview(
      VehicleOwnerRequest request)
      throws SQLException;
  void delete(DeleteVehicleRequest request);
  void changeVehicle(VehicleChangeRequest request) throws SQLException;
}
