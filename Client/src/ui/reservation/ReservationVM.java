package ui.reservation;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationRequestByIdType;
import dtos.vehicle.VehicleDisplayDto;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Date;
import model.entities.reservation.Reservation;
import networking.reservation.ReservationClient;
import startup.ViewHandler;
import state.AppState;
import ui.popup.MessageType;
import networking.reservation.ReservationSubscriber;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationVM
{
  private final ObservableList<VehicleFx> vehicles = FXCollections.observableArrayList();
  private final ObjectProperty<VehicleFx> selectedVehicle = new SimpleObjectProperty<>();

  private final FilteredList<VehicleFx> filteredVehicles = new FilteredList<>(
      vehicles, p -> true);

  // Realated to fields
  private final ObjectProperty<LocalDate> datePickerProp = new SimpleObjectProperty<>();
  private final StringProperty profileTextRedirectProp = new SimpleStringProperty();
  private final StringProperty conditionProp = new SimpleStringProperty();
  private final StringProperty colorProp = new SimpleStringProperty();
  private final StringProperty ownerEmailProp = new SimpleStringProperty();
  private final StringProperty bikeTypeProp = new SimpleStringProperty();
  private final StringProperty speedProp = new SimpleStringProperty();
  private final StringProperty rangeProp = new SimpleStringProperty();
  private final ObjectProperty<LocalDate> reservationDateProp = new SimpleObjectProperty<>(
      LocalDate.now());
  private final StringProperty finalPriceProp = new SimpleStringProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final BooleanProperty reservationSuccess = new SimpleBooleanProperty(false);

  // Additional info
  private final IntegerProperty idProp = new SimpleIntegerProperty();

  // Search query
  private final StringProperty searchQuery = new SimpleStringProperty("");

  // Visibility properties
  private final BooleanProperty dkkShowerVisibility = new SimpleBooleanProperty();
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
  private final BooleanProperty datePickerVisibility = new SimpleBooleanProperty();
  private final BooleanProperty priceFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty priceLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty datePickerLabelVisibility = new SimpleBooleanProperty();
  private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
  private ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();

  private final ReservationClient reservationService;


  public ReservationVM(ReservationClient reservationService)
  {
    this.reservationService = reservationService;
    try
    {
      new ReservationSubscriber("localhost", 2910, () -> loadVehicles());
    }
    catch (IOException e)
    {
    }
  }


  public void addReservation()
  {
    messageProp.set("");
    VehicleFx v = selectedVehicle.get();
    if (v == null || startDate == null || endDate == null)
    {
      return;
    }
    try
    {
      LocalDate start = startDate.get();
      LocalDate end = endDate.get();
      String reservedEmail = AppState.getCurrentUser().email();
      double price = Double.parseDouble(finalPriceProp.get());
      ReservationRequest request = new ReservationRequest(idProp.get(),
          v.typePropProperty().get(), ownerEmailProp.get(), reservedEmail,
          new Date(start.getDayOfMonth(), start.getMonth().getValue(),
              start.getYear()),
          new Date(end.getDayOfMonth(), end.getMonth().getValue(),
              end.getYear()), price);

      reservationService.addNewReservation(request);
      reservationSuccess.set(true);

      messageProp.set("Success");
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
      reservationSuccess.set(false);
    }
  }

  public void loadVehicles()
  {
    try
    {
      List<VehicleDisplayDto> loadedVehicles = reservationService.getVehicles();
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
        dkkShowerVisibility.set(false);
        datePickerLabelVisibility.set(true);
        priceFieldVisibility.set(false);
        priceLabelVisibility.set(true);
        datePickerVisibility.set(true);
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
        dkkShowerVisibility.set(false);
        datePickerLabelVisibility.set(true);
        priceFieldVisibility.set(false);
        priceLabelVisibility.set(true);
        datePickerVisibility.set(true);
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
        dkkShowerVisibility.set(false);
        datePickerLabelVisibility.set(true);
        priceFieldVisibility.set(false);
        priceLabelVisibility.set(true);
        datePickerVisibility.set(true);
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
        dkkShowerVisibility.set(false);
        datePickerLabelVisibility.set(false);
        priceFieldVisibility.set(false);
        priceLabelVisibility.set(false);
        datePickerVisibility.set(false);
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
    finalPriceProp.set("");
    ownerEmailProp.set("");
    conditionProp.set("");
    colorProp.set("");
    speedProp.set("");
    rangeProp.set("");
    bikeTypeProp.set("");
    datePickerProp.set(LocalDate.now());

  }

  // Additional info
  public IntegerProperty getIdProp()
  {
    return idProp;
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


  public StringProperty finalPriceProperty()
  {
    return finalPriceProp;
  }

  public ObjectProperty<LocalDate> datePickerProp()
  {
    return datePickerProp;
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

  public BooleanProperty datePickerVisibility()
  {
    return datePickerVisibility;
  }

  public BooleanProperty datePickerLabelVisibility()
  {
    return datePickerLabelVisibility;
  }

  public BooleanProperty priceFieldVisibility()
  {
    return priceFieldVisibility;
  }

  public BooleanProperty priceLabelVisibility()
  {
    return priceLabelVisibility;
  }

  public StringProperty searchQueryProperty()
  {
    return searchQuery;
  }

  public FilteredList<VehicleFx> getFilteredVehicles()
  {
    return filteredVehicles;
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public ObjectProperty<LocalDate> startDateProperty()
  {
    return startDate;
  }

  public ObjectProperty<LocalDate> endDateProperty()
  {
    return endDate;
  }
  public BooleanProperty reservationSuccessProperty() {
    return reservationSuccess;
  }

  public StringProperty profileTextRedirectProperty()
  {
    return profileTextRedirectProp;
  }

  public BooleanProperty dkkShowerVisibility()
  {
    return dkkShowerVisibility;
  }

  public void setFinalPrice()
  {
    Date date1 = new Date(startDate.get().getDayOfMonth(), startDate.get().getMonthValue(), startDate.get().getYear());
    Date date2 = new Date(endDate.get().getDayOfMonth(), endDate.get().getMonthValue(), endDate.get().getYear());
    int period = Date.calculatePeriod(date1,date2);
    double totalPrice = period * selectedVehicle.get().pricePerDayPropProperty().get();
    priceFieldVisibility.set(true);
    dkkShowerVisibility.set(true);
    finalPriceProp.set(Double.toString(totalPrice));
  }

  public void setProfileInitials()
  {
    String firstname = AppState.getCurrentUser().firstName();
    String lastname = AppState.getCurrentUser().lastName();
    profileTextRedirectProp.set("" + firstname.charAt(0) + lastname.charAt(0));
  }

  public ArrayList<Reservation> getReservationsByTypeAndId(
      ReservationRequestByIdType request)
  {
    List<ReservationDto> reservationDtos = reservationService.getReservationsByTypeAndId(request);
    ArrayList<Reservation> reservations = new ArrayList<>();
    ReservationDto temp;
    for (int i = 0; i < reservationDtos.size(); i++)
    {
      temp = reservationDtos.get(i);
      reservations.add(new Reservation(temp.vehicleId(), temp.vehicleType(), temp.ownerEmail(), temp.reservedByEmail(), temp.startDate(), temp.endDate(), temp.price()));
    }
    return reservations;
  }

  public void update()
  {
    reservationService.updateVehicleState();
  }

}