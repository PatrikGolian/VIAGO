package ui.reservation;

import dtos.user.ViewUsers;
import dtos.vehicle.VehicleDisplayDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.reservation.ReservationClient;
import services.reservation.ReservationService;
import startup.ViewHandler;
import ui.popup.MessageType;
import ui.viewusers.UserFx;

import java.util.List;

public class ReservationVM
{
  private final ObservableList<VehicleFx> vehicles = FXCollections.observableArrayList();
  private final ReservationClient reservationService;

  public ReservationVM(ReservationClient reservationService)
  {
    this.reservationService = reservationService;
  }

  public void addReservations()
  {


  }
  public void loadVehicles()
  {
    try
    {
      List<VehicleDisplayDto> loadedVehicles = reservationService.getVehicles();
      for (VehicleDisplayDto vehicle : loadedVehicles)
      {
        vehicles.add(new VehicleFx(vehicle));
      }
    }
    catch (Exception e)
    {
      ViewHandler.popupMessage(MessageType.ERROR, e.getMessage());
    }
  }

  public ObservableList<VehicleFx> getVehicleList()
  {
    return vehicles;
  }

}
