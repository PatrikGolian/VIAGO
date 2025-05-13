package networking.myvehicles;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationReserveRequest;
import dtos.vehicle.VehicleDisplayDto;
import dtos.vehicle.VehicleOwnerRequest;

import java.util.List;

public interface MyVehiclesClient
{
  List<VehicleDisplayDto> getVehicles(VehicleOwnerRequest request);
}
