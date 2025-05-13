package networking.requesthandlers;

import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.vehicle.DeleteVehicleRequest;
import dtos.vehicle.VehicleOwnerRequest;
import networking.readerswriters.ReadWrite;
import networking.readerswriters.Reader;
import networking.readerswriters.Writer;
import services.myvehicles.MyVehiclesService;
import services.reservation.ReservationService;

import java.sql.SQLException;

public class MyVehiclesRequestHandler implements RequestHandler
{
  private final MyVehiclesService myVehiclesService;
  private final ReadWrite lock;

  public MyVehiclesRequestHandler(MyVehiclesService myVehiclesService, ReadWrite sharedResource)
  {
    this.myVehiclesService = myVehiclesService;
    this.lock = sharedResource;
  }

  public Object handle(String action, Object payload) throws SQLException
  {
    System.out.println("MyVehiclesRequestHandler got action = "+action);
    switch (action)
    {

      case "view_vehicles" ->
      {
        Reader<Object> reader = new Reader<>(lock, () -> {
          try
          {
            return myVehiclesService.getVehiclesOverview(
                (VehicleOwnerRequest) payload);
          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
        Thread thread = new Thread(reader);
        thread.start();
        try
        {
          thread.join();
        }
        catch (InterruptedException e)
        {
          Thread.currentThread().interrupt();
        }
        return reader.getResult();
      }
      case "delete_vehicle" -> {
        Writer writer = new Writer(lock, () -> {
          myVehiclesService.delete((DeleteVehicleRequest) payload);
        });
        Thread thread = new Thread(writer);
        thread.start();
        try {
          thread.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        return Boolean.TRUE;
      }
    }
    return null;
  }
}
