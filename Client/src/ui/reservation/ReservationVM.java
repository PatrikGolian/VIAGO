package ui.reservation;

import dtos.vehicle.VehicleDisplayDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.reservation.ReservationClient;
import startup.ViewHandler;
import ui.popup.MessageType;

import java.util.List;

public class ReservationVM
{
  private final ObservableList<VehicleFx> vehicles = FXCollections.observableArrayList();
  private final ReservationClient reservationService;

  public ReservationVM(ReservationClient reservationService)
  {
    this.reservationService = reservationService;

    loadVehicles();
  }

  public void addReservations()
  {
    //reservationService.addNewReservation();
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
      e.printStackTrace();
      ViewHandler.popupMessage(MessageType.ERROR, e.getMessage());
    }
  }

  public ObservableList<VehicleFx> getVehicleList()
  {
    return vehicles;
  }


}
