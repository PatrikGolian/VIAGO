package networking.requesthandlers;

import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationRequestByIdType;
import dtos.reservation.ReservationReserveRequest;
import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.VehicleDisplayDto;
import dtos.vehicle.VehicleOwnerRequest;
import networking.readerswriters.ReadWrite;
import networking.readerswriters.Reader;
import networking.readerswriters.Writer;
import services.reservation.ReservationService;
import services.vehicle.VehicleService;

import java.sql.SQLException;

public class ReservationRequestHandler implements RequestHandler
{
  private final ReservationService reservationService;
  private final VehicleService vehicleService;
  private final ReadWrite lock;


  public ReservationRequestHandler(ReservationService reservationService, VehicleService vehicleService, ReadWrite sharedResource)
  {
    this.reservationService = reservationService;
    this.vehicleService = vehicleService;
    this.lock = sharedResource;
  }



  @Override public Object handle(String action, Object payload)
      throws SQLException
  {
    switch (action)
    {
      case "reserve" -> {

        Writer writer = new Writer(lock, () -> {
          reservationService.addNewReservation((ReservationRequest) payload);
        });
        Thread thread = new Thread(writer);
        thread.start();
        try {
          thread.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }


      }
      case "delete_allReservation" ->{
        Writer writer = new Writer(lock, () -> {
          reservationService.deleteAll((ReservationReserveRequest) payload);
        });
        Thread thread = new Thread(writer);
        thread.start();
        try {
          thread.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      case "view_vehicles" ->
      {
        Reader<Object> reader = new Reader<>(lock, () -> {
          try
          {
            return vehicleService.getVehiclesOverview();
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
      case "getReservations" ->
      {
        Reader<Object> reader = new Reader<>(lock, () -> {
          try
          {
            return reservationService.getReservationsByTypeAndId((ReservationRequestByIdType) payload);
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
      case "update_state" -> {
        Writer writer = new Writer(lock, () -> {
          reservationService.updateVehicleState();
        });
        Thread thread = new Thread(writer);
        thread.start();
        try {
          thread.join();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

      }
    }
    return  null;
  }
}
