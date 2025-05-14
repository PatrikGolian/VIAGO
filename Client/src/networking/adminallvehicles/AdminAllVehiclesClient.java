package networking.adminallvehicles;

import dtos.vehicle.DeleteVehicleRequest;
import dtos.vehicle.VehicleDisplayDto;

import java.util.List;

public interface AdminAllVehiclesClient
{
  List<VehicleDisplayDto> getVehicles();
  void delete(DeleteVehicleRequest request);
}
