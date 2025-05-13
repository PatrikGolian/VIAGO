package networking.requesthandlers;

import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.vehicle.DeleteVehicleRequest;
import dtos.vehicle.VehicleOwnerRequest;
import services.myvehicles.MyVehiclesService;
import services.reservation.ReservationService;

import java.sql.SQLException;

public class MyVehiclesRequestHandler implements RequestHandler
{
  private final MyVehiclesService myVehiclesService;

  public MyVehiclesRequestHandler(MyVehiclesService myVehiclesService)
  {
    this.myVehiclesService = myVehiclesService;
  }

  public Object handle(String action, Object payload) throws SQLException
  {
    System.out.println("MyVehiclesRequestHandler got action = "+action);
    switch (action)
    {

      case "view_vehicles" ->
      {
        return myVehiclesService.getVehiclesOverview(
            (VehicleOwnerRequest) payload);
      }
      case "delete_vehicle" -> {
        myVehiclesService.delete((DeleteVehicleRequest) payload);
        return Boolean.TRUE;
      }
    }
    return null; // just a default return value. Some actions above may return stuff.
  }
}
