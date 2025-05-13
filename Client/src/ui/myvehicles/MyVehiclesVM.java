package ui.myvehicles;

import dtos.reservation.ReservationReserveRequest;
import dtos.vehicle.VehicleDisplayDto;
import dtos.vehicle.VehicleOwnerRequest;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.myvehicles.MyVehiclesClient;
import networking.reservation.ReservationClient;
import startup.ViewHandler;
import state.AppState;
import ui.popup.MessageType;
import ui.reservation.VehicleFx;

import java.util.List;

public class MyVehiclesVM
{
  private final ObservableList<VehicleFx> vehicles = FXCollections.observableArrayList();
  private final ObjectProperty<VehicleFx> selectedVehicle = new SimpleObjectProperty<>();

  private final StringProperty profileTextRedirectProp = new SimpleStringProperty();

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

  private final MyVehiclesClient myVehiclesClient;

  public MyVehiclesVM(MyVehiclesClient myVehiclesClient)
  {
    this.myVehiclesClient = myVehiclesClient;
    loadVehicles();
  }
  public void loadVehicles()
  {
    try
    {
      String ownerEmail = AppState.getCurrentUser().email();
      List<VehicleDisplayDto> loadedVehicles = myVehiclesClient.getVehicles(new VehicleOwnerRequest(ownerEmail));
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
