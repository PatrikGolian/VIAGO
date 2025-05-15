package ui.adminallvehicles;

import dtos.vehicle.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import networking.adminallvehicles.AdminAllVehiclesClient;
import networking.adminallvehicles.AllVehiclesSubscriber;
import networking.reservation.ReservationClient;
import networking.reservation.ReservationSubscriber;
import startup.ViewHandler;
import state.AppState;
import ui.popup.MessageType;
import ui.reservation.VehicleFx;

import java.io.IOException;
import java.util.List;

public class AdminAllVehiclesVM
{

  private final AdminAllVehiclesClient adminAllVehiclesClient;
  private final ObservableList<VehicleFx> vehicles = FXCollections.observableArrayList();
  private final ObjectProperty<VehicleFx> selectedVehicle = new SimpleObjectProperty<>();
  private final FilteredList<VehicleFx> filteredVehicles = new FilteredList<>(
      vehicles, p -> true);

  private final StringProperty profileTextRedirectProp = new SimpleStringProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty bikeTypeProp = new SimpleStringProperty();
  private final StringProperty speedProp = new SimpleStringProperty();
  private final StringProperty rangeProp = new SimpleStringProperty();
  private final IntegerProperty idProp = new SimpleIntegerProperty();

  private final BooleanProperty speedLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty rangeLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty bikeTypeLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty speedFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty rangeFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty bikeTypeFieldVisibility = new SimpleBooleanProperty();

  public AdminAllVehiclesVM(AdminAllVehiclesClient adminAllVehiclesClient)
  {
    this.adminAllVehiclesClient = adminAllVehiclesClient;
    try
    {
      new AllVehiclesSubscriber("localhost", 2910, () -> loadVehicles());
    }
    catch (IOException e)
    {
    }
    loadVehicles();
  }



  public void loadVehicles()
  {
    try
    {
      List<VehicleDisplayDto> loadedVehicles = adminAllVehiclesClient.getVehicles();
      vehicles.clear();
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


  public ObservableList<VehicleFx> getVehicleList()
  {
    return vehicles;
  }

  public FilteredList<VehicleFx> getFilteredVehicles()
  {
    return filteredVehicles;
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

    } catch (Exception e) {
      messageProp.set("An error occurred while trying to delete the vehicle.");

    }
  }

  private void clearFields()
  {
    speedProp.set("");
    rangeProp.set("");
    bikeTypeProp.set("");
  }
  public void setVisibility()
  {
    clearFields();
    VehicleFx vehicleFx = selectedVehicle.get();
    if (vehicleFx == null)
    {
      return;
    }
    switch (vehicleFx.typePropProperty().get())
    {
      case "scooter" ->
      {
        speedFieldVisibility.set(true);
        speedLabelVisibility.set(true);
        rangeFieldVisibility.set(true);
        rangeLabelVisibility.set(true);
        bikeTypeLabelVisibility.set(false);
        bikeTypeFieldVisibility.set(false);
      }
      case "bike" ->
      {
        speedFieldVisibility.set(false);
        speedLabelVisibility.set(false);
        rangeFieldVisibility.set(false);
        rangeLabelVisibility.set(false);
        bikeTypeLabelVisibility.set(true);
        bikeTypeFieldVisibility.set(true);
      }
      case "e-bike" ->
      {
        speedFieldVisibility.set(true);
        speedLabelVisibility.set(true);
        rangeFieldVisibility.set(true);
        rangeLabelVisibility.set(true);
        bikeTypeLabelVisibility.set(true);
        bikeTypeFieldVisibility.set(true);
      }
      default ->
      {
        speedFieldVisibility.set(false);
        speedLabelVisibility.set(false);
        rangeFieldVisibility.set(false);
        rangeLabelVisibility.set(false);
        bikeTypeLabelVisibility.set(false);
        bikeTypeFieldVisibility.set(false);
      }
    }
  }

  public void setProfileInitials()
  {
    String firstname = AppState.getCurrentUser().firstName();
    String lastname = AppState.getCurrentUser().lastName();
    profileTextRedirectProp.set("" + firstname.charAt(0) + lastname.charAt(0));
  }

  public BooleanProperty getSpeedLabelVisibility()
  {
    return speedLabelVisibility;
  }

  public BooleanProperty getSpeedFieldVisibility()
  {
    return speedFieldVisibility;
  }
  public IntegerProperty getIdProp()
  {
    return idProp;
  }
  public StringProperty bikeTypeProperty()
  {
    return bikeTypeProp;
  }
  public Property<String> messageLabelProperty()
  {
    return messageProp;
  }

  public StringProperty speedProperty()
  {
    return speedProp;
  }

  public StringProperty rangeProperty()
  {
    return rangeProp;
  }


  public BooleanProperty getRangeLabelVisibility()
  {
    return rangeLabelVisibility;
  }

  public BooleanProperty getRangeFieldVisibility()
  {
    return rangeFieldVisibility;
  }

  public BooleanProperty getBikeTypeLabelVisibility()
  {
    return bikeTypeLabelVisibility;
  }

  public BooleanProperty getBikeTypeFieldVisibility()
  {
    return bikeTypeFieldVisibility;
  }
  public StringProperty profileTextRedirectProperty()
  {
    return profileTextRedirectProp;
  }
  public ObjectProperty<VehicleFx> selectedVehicleProperty()
  {
    return selectedVehicle;
  }
}
