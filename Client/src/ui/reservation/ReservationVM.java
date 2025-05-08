package ui.reservation;

import dtos.reservation.ReservationRequest;
import dtos.vehicle.VehicleDataDto;
import dtos.vehicle.VehicleDisplayDto;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Date;
import networking.reservation.ReservationClient;
import startup.ViewHandler;
import state.AppState;
import ui.popup.MessageType;

import java.time.LocalDate;
import java.util.List;

public class ReservationVM
{
  private final ObservableList<VehicleFx> vehicles = FXCollections.observableArrayList();
  private final ObjectProperty<VehicleFx> selectedVehicle = new SimpleObjectProperty<>();

  // Realated to fields
  private final StringProperty conditionProp = new SimpleStringProperty();
  private final StringProperty colorProp = new SimpleStringProperty();
  private final StringProperty ownerEmailProp = new SimpleStringProperty();
  private final StringProperty bikeTypeProp = new SimpleStringProperty();
  private final StringProperty speedProp = new SimpleStringProperty();
  private final StringProperty rangeProp = new SimpleStringProperty();
  private final ObjectProperty<LocalDate> reservationDateProp = new SimpleObjectProperty<>(
      LocalDate.now());
  private final StringProperty finalPriceProp = new SimpleStringProperty();


  // Additional info
  private final StringProperty reservedEmailProp = new SimpleStringProperty();
  private final IntegerProperty idProp = new SimpleIntegerProperty();

  // Visibility properties
  private final BooleanProperty conditionFieldVisibility = new SimpleBooleanProperty();

  private final BooleanProperty colorFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty ownerEmailFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty conditionLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty colorLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty ownerEmailLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty speedLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty rangeLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty bikeTypeLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty speedFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty rangeFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty bikeTypeFieldVisibility = new SimpleBooleanProperty();

  private final ReservationClient reservationService;

  public ReservationVM(ReservationClient reservationService)
  {
    this.reservationService = reservationService;

    loadVehicles();
  }

  public void addReservation()
  {
    VehicleFx v = selectedVehicle.get();
    if (v == null)
    {
      return;
    }
    String reservedEmail = AppState.getCurrentUser().email();
    double price = Double.parseDouble(finalPriceProp.get());
    ReservationRequest request = new ReservationRequest(idProp.get(),
        v.typePropProperty().get(), ownerEmailProp.get(), reservedEmail,
        new Date(),
        new Date(),
        price);

    //reservationService.addNewReservation(new ReservationRequest());
  }

  public void loadVehicles()
  {
    try
    {
      List<VehicleDisplayDto> loadedVehicles = reservationService.getVehicles();
      //List<VehicleDataDto> vehicleDataDtos = reservationService.getVehicleDatas();
//      if (vehicleDataDtos.size()!= vehicleDataDtos.size())
//      { System.out.println("DTO sizes do not match!");}
      for (VehicleDisplayDto vehicle : loadedVehicles) //(int i = 0; i < loadedVehicles.size(); i++)
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

  public ObjectProperty<VehicleFx> selectedVehicleProperty()
  {
    return selectedVehicle;
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
        conditionFieldVisibility.set(true);
        conditionLabelVisibility.set(true);
        colorFieldVisibility.set(true);
        colorLabelVisibility.set(true);
        ownerEmailFieldVisibility.set(true);
        ownerEmailLabelVisibility.set(true);
        speedFieldVisibility.set(true);
        speedLabelVisibility.set(true);
        rangeFieldVisibility.set(true);
        rangeLabelVisibility.set(true);
        bikeTypeLabelVisibility.set(false);
        bikeTypeFieldVisibility.set(false);
      }
      case "bike" ->
      {
        conditionFieldVisibility.set(true);
        conditionLabelVisibility.set(true);
        colorFieldVisibility.set(true);
        colorLabelVisibility.set(true);
        ownerEmailFieldVisibility.set(true);
        ownerEmailLabelVisibility.set(true);
        speedFieldVisibility.set(false);
        speedLabelVisibility.set(false);
        rangeFieldVisibility.set(false);
        rangeLabelVisibility.set(false);
        bikeTypeLabelVisibility.set(true);
        bikeTypeFieldVisibility.set(true);
      }
      case "e-bike" ->
      {
        conditionFieldVisibility.set(true);
        conditionLabelVisibility.set(true);
        colorFieldVisibility.set(true);
        colorLabelVisibility.set(true);
        ownerEmailFieldVisibility.set(true);
        ownerEmailLabelVisibility.set(true);
        speedFieldVisibility.set(true);
        speedLabelVisibility.set(true);
        rangeFieldVisibility.set(true);
        rangeLabelVisibility.set(true);
        bikeTypeLabelVisibility.set(true);
        bikeTypeFieldVisibility.set(true);
      }
      default ->
      {
        conditionFieldVisibility.set(false);
        conditionLabelVisibility.set(false);
        colorFieldVisibility.set(false);
        colorLabelVisibility.set(false);
        ownerEmailFieldVisibility.set(false);
        ownerEmailLabelVisibility.set(false);
        speedFieldVisibility.set(false);
        speedLabelVisibility.set(false);
        rangeFieldVisibility.set(false);
        rangeLabelVisibility.set(false);
        bikeTypeLabelVisibility.set(false);
        bikeTypeFieldVisibility.set(false);
      }
    }
  }

  private void clearFields()
  {
    conditionProp.set("");
    colorProp.set("");
    speedProp.set("");
    rangeProp.set("");
    bikeTypeProp.set("");
  }

  // Addtional info
  public IntegerProperty getIdProp()
  {
    return idProp;
  }

  public StringProperty getFinalPriceProp()
  {
    return finalPriceProp;
  }

  // Related to fields
  public StringProperty conditionProperty()
  {
    return conditionProp;
  }

  public StringProperty colorProperty()
  {
    return colorProp;
  }

  public StringProperty getOwnerEmailProp()
  {
    return ownerEmailProp;
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

  public ObjectProperty<LocalDate> reservationDateProperty()
  {
    return reservationDateProp;
  }
  public StringProperty finalPriceProperty()
  {
    return finalPriceProp;
  }

  // Visibility properties

  public BooleanProperty getSpeedLabelVisibility()
  {
    return speedLabelVisibility;
  }

  public BooleanProperty getSpeedFieldVisibility()
  {
    return speedFieldVisibility;
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

  public BooleanProperty conditionFieldVisibilityProperty()
  {
    return conditionFieldVisibility;
  }

  public BooleanProperty colorFieldVisibilityProperty()
  {
    return colorFieldVisibility;
  }

  public BooleanProperty ownerEmailFieldVisibilityProperty()
  {
    return ownerEmailFieldVisibility;
  }

  public BooleanProperty conditionLabelVisibilityProperty()
  {
    return conditionLabelVisibility;
  }

  public BooleanProperty colorLabelVisibilityProperty()
  {
    return colorLabelVisibility;
  }

  public BooleanProperty ownerEmailLabelVisibilityProperty()
  {
    return ownerEmailLabelVisibility;
  }
}
