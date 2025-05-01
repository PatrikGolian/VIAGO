package networking.requesthandlers;

import dtos.reservation.ReservationRequest;
import dtos.vehicle.VehicleDisplayDto;
import services.reservation.ReservationService;
import services.vehicle.VehicleService;

import java.sql.SQLException;

public class ReservationRequestHandler implements RequestHandler
{
  private final ReservationService reservationService;
  private final VehicleService vehicleService;


  public ReservationRequestHandler(ReservationService reservationService, VehicleService vehicleService)
  {
    this.reservationService = reservationService;
    this.vehicleService = vehicleService;
  }

  @Override public Object handle(String action, Object payload)
      throws SQLException
  {
    switch (action)
    {
      case "reserve" -> reservationService.addNewReservation((ReservationRequest) payload);
      case "view_vehicles" ->
      {
        return vehicleService.getVehiclesOverview();
      }
    }
    return  null;
  }
}
