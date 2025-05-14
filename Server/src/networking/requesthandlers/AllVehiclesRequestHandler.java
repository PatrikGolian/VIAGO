package networking.requesthandlers;

import dtos.vehicle.DeleteVehicleRequest;
import dtos.vehicle.VehicleOwnerRequest;
import networking.readerswriters.ReadWrite;
import networking.readerswriters.Reader;
import networking.readerswriters.Writer;
import services.allvehicles.AllVehiclesService;
import services.myvehicles.MyVehiclesService;

import java.sql.SQLException;

public class AllVehiclesRequestHandler implements RequestHandler
{
  private final AllVehiclesService allVehiclesService;
  private final ReadWrite lock;

  public AllVehiclesRequestHandler(AllVehiclesService allVehiclesService, ReadWrite sharedResource)
  {
    this.allVehiclesService = allVehiclesService;
    this.lock = sharedResource;
  }
  public Object handle(String action, Object payload) throws SQLException
  {
    System.out.println("AllVehiclesRequestHandler got action = "+action);
    switch (action)
    {

      case "view_vehicles" ->
      {
        Reader<Object> reader = new Reader<>(lock, () -> {
          try
          {
            return allVehiclesService.getVehiclesOverview();
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
          allVehiclesService.delete((DeleteVehicleRequest) payload);
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
