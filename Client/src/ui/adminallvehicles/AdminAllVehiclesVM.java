package ui.adminallvehicles;

import dtos.vehicle.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.adminallvehicles.AdminAllVehiclesClient;
import networking.reservation.ReservationClient;
import networking.reservation.ReservationSubscriber;
import startup.ViewHandler;
import ui.popup.MessageType;
import ui.reservation.VehicleFx;

import java.io.IOException;
import java.util.List;

public class AdminAllVehiclesVM
{

  private final AdminAllVehiclesClient adminAllVehiclesClient;
  private final ObservableList<VehicleFx> vehicles = FXCollections.observableArrayList();


  private final StringProperty messageProp = new SimpleStringProperty();

  public AdminAllVehiclesVM(AdminAllVehiclesClient adminAllVehiclesClient)
  {
    this.adminAllVehiclesClient = adminAllVehiclesClient;
    /*try
    {
      new ReservationSubscriber("localhost", 2910, () -> loadVehicles());
    }
    catch (IOException e)
    {
    }*/
  }



  public void loadVehicles()
  {
    try
    {
      List<VehicleDisplayDto> loadedVehicles = adminAllVehiclesClient.getVehicles();
      //vehicles.clear();
      for (VehicleDisplayDto vehicle : loadedVehicles) // (int i = 0; i < loadedVehicles.size(); i++)
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


  public void deleteVehicle(VehicleFx vehicleFx)
  {
    if (vehicleFx == null) {
      return;
    }

    try {
      String state = vehicleFx.statePropProperty().get();
      if (!"Available".equalsIgnoreCase(state)) {
        messageProp.set("You can only delete vehicles that are available.");
        return;
      }

      int id = vehicleFx.idPropProperty().get();
      String type = vehicleFx.typePropProperty().get();
      String brand = vehicleFx.brandPropProperty().get();
      String model = vehicleFx.modelPropProperty().get();
      String condition = vehicleFx.conditionPropProperty().get();
      String color = vehicleFx.colorPropProperty().get();
      double pricePerDay = vehicleFx.pricePerDayPropProperty().get();
      String ownerEmail = vehicleFx.ownerPropProperty().get();

      DeleteVehicleRequest request;

      switch (type.toLowerCase()) {
        case "bike" -> {
          String bikeType = vehicleFx.bikeTypePropProperty().get();
          request = new DeleteBikeRequest(id, type, brand, model, condition, color, pricePerDay, bikeType, ownerEmail, state);
        }
        case "e-bike" -> {
          String bikeType = vehicleFx.bikeTypePropProperty().get();
          int maxSpeed = Integer.parseInt(vehicleFx.maxSpeedPropProperty().get());
          int oneChargeRange = Integer.parseInt(vehicleFx.rangeProperty().get());
          request = new DeleteEBikeRequest(id, type, brand, model, condition, color, pricePerDay, maxSpeed, oneChargeRange,bikeType, ownerEmail, state);
        }
        case "scooter" -> {
          int maxSpeed = Integer.parseInt(vehicleFx.maxSpeedPropProperty().get());
          int oneChargeRange = Integer.parseInt(vehicleFx.rangeProperty().get());
          request = new DeleteScooterRequest(id, type, brand, model, condition, color, pricePerDay, maxSpeed, oneChargeRange, ownerEmail, state);
        }
        default -> {
          messageProp.set("Unknown vehicle type: " + type);
          return;
        }
      }

      adminAllVehiclesClient.delete(request);
      messageProp.set("Vehicle deleted successfully.");
    } catch (NumberFormatException e) {
      messageProp.set("Invalid number format in speed or range.");
      e.printStackTrace();
    } catch (Exception e) {
      messageProp.set("An error occurred while trying to delete the vehicle.");
      e.printStackTrace();
    }
  }


}
