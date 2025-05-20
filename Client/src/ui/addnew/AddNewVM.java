package ui.addnew;

import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;
import javafx.beans.Observable;
import javafx.beans.property.*;
import model.exceptions.ValidationException;
import networking.addnew.AddNewVehicleClient;
import networking.user.BlacklistSubscriber;
import startup.ViewHandler;
import startup.ViewType;
import state.AppState;
import ui.popup.MessageType;
import utils.StringUtils;

import java.io.IOException;

public class AddNewVM
{
  private final StringProperty profileTextRedirectProp = new SimpleStringProperty();
  private final StringProperty typeProp = new SimpleStringProperty();
  private final IntegerProperty idProp = new SimpleIntegerProperty();
  private final StringProperty brandProp = new SimpleStringProperty();
  private final StringProperty modelProp = new SimpleStringProperty();
  private final StringProperty conditionProp = new SimpleStringProperty();
  private final StringProperty colorProp = new SimpleStringProperty();
  private final StringProperty priceProp = new SimpleStringProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty speedProp = new SimpleStringProperty();
  private final StringProperty rangeProp = new SimpleStringProperty();
  private final StringProperty bikeTypeProp = new SimpleStringProperty();
  private final BooleanProperty speedLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty rangeLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty bikeTypeLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty speedFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty rangeFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty bikeTypeFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty disableAddButtonProp = new SimpleBooleanProperty(
      true);

  private final AddNewVehicleClient addNewService;

  public AddNewVM(AddNewVehicleClient addNewService)
  {
    this.addNewService = addNewService;

    typeProp.addListener(this::updateAddButtonState);
    brandProp.addListener(this::updateAddButtonState);
    modelProp.addListener(this::updateAddButtonState);
    conditionProp.addListener(this::updateAddButtonState);
    colorProp.addListener(this::updateAddButtonState);
    priceProp.addListener(this::updateAddButtonState);
    speedProp.addListener(this::updateAddButtonState);
    rangeProp.addListener(this::updateAddButtonState);
    bikeTypeProp.addListener(this::updateAddButtonState);
  }

  public void add()
  {

    String email = AppState.getCurrentUser().email();
    messageProp.set("");
    double price;

    if (!(typeProp.get().equals("bike") || typeProp.get().equals("e-bike")
        || typeProp.get().equals("scooter")))
    {
      messageProp.set("Type has to be either bike, e-bike or scooter");
      return;
    }

    if (!brandProp.get().matches("[a-zA-Z ]+"))
    {
      messageProp.set("Brand can contain only letters");
      return;
    }

    if (!((conditionProp.get().equals("used") || conditionProp.get()
        .equals("good") || conditionProp.get().equals("new"))))
    {
      messageProp.set("Condition has to be either used, good or new");
      return;
    }

    if (!colorProp.get().matches("[a-zA-Z ]+"))
    {
      messageProp.set("Color can only contain letters");
      return;
    }

    try
    {
      price = Double.parseDouble(priceProp.get());
      if (price <= 0)
      {
        messageProp.set("Price has to be a positive number");
        return;
      }
    }
    catch (NumberFormatException e)
    {
      messageProp.set("Price can't contain letters");
      return;
    }

      switch (typeProp.get())
      {
        case "scooter" ->
        {

          int speed;
          int range;

          try
          {
            speed = Integer.parseInt(speedProp.get());
            if (speed <= 0)
            {
              messageProp.set("Max speed has to be a positive number");
              return;
            }
          }
          catch (NumberFormatException e)
          {
            messageProp.set("Max speed can't contain letters.");
            return;
          }
          try
          {
            range = Integer.parseInt(rangeProp.get());
            if (range <= 0)
            {
              messageProp.set("Range has to be a positive number");
              return;
            }
          }
          catch (NumberFormatException e)
          {
            messageProp.set("Range can't contain letters.");
            return;
          }
          addNewService.addNewVehicle(
              new AddNewScooterRequest(idProp.get(), typeProp.get(),
                  brandProp.get(), modelProp.get(), conditionProp.get(),
                  colorProp.get(), price, speed, range, email, "Available"));

          messageProp.set("Success");
          // clear fields
          typeProp.set("");
          clearFields();
        }

        case "bike" ->
        {
          if (!bikeTypeProp.get().matches("[a-zA-Z ]+"))
          {
            messageProp.set("Bike Type can only contain letters");
            return;
          }

          addNewService.addNewVehicle(
              new AddNewBikeRequest(idProp.get(), typeProp.get(),
                  brandProp.get(), modelProp.get(), conditionProp.get(),
                  colorProp.get(), price, bikeTypeProp.get(), email,
                  "Available"));
          messageProp.set("Success");
          // clear fields
          typeProp.set("");
          clearFields();
        }

        case "e-bike" ->
        {
          int speed;
          int range;
          try
          {
            speed = Integer.parseInt(speedProp.get());
            if (speed <= 0)
            {
              messageProp.set("Max speed has to be a positive number");
              return;
            }
          }
          catch (NumberFormatException e)
          {
            messageProp.set("Max speed can't contain letters.");
            return;
          }
          try
          {
            range = Integer.parseInt(rangeProp.get());
            if (range <= 0)
            {
              messageProp.set("Range has to be a positive number");
              return;
            }
          }
          catch (NumberFormatException e)
          {
            messageProp.set("Range can't contain letters.");
            return;
          }
          if (!bikeTypeProp.get().matches("[a-zA-Z ]+"))
          {
            messageProp.set("Bike Type can only contain letters");
            return;
          }
          addNewService.addNewVehicle(
              new AddNewEBikeRequest(idProp.get(), typeProp.get(),
                  brandProp.get(), modelProp.get(), conditionProp.get(),
                  colorProp.get(), price, speed, range, bikeTypeProp.get(),
                  email, "Available"));

          messageProp.set("Success");
          // clear fields
          typeProp.set("");
          clearFields();
        }
        default ->
        {
          messageProp.set("No such vehicle type found! Needs to be scooter, bike, e-bike");
          return;
        }
      }
  }

  public void setVisibility()
  {
    clearFields();

    switch (typeProp.get())
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

  private void clearFields()
  {
    brandProp.set("");
    modelProp.set("");
    conditionProp.set("");
    colorProp.set("");
    priceProp.set("");
    speedProp.set("");
    rangeProp.set("");
    bikeTypeProp.set("");
  }

  public StringProperty typeProperty()
  {
    return typeProp;
  }

  public StringProperty brandProperty()
  {
    return brandProp;
  }

  public StringProperty modelProperty()
  {
    return modelProp;
  }

  public StringProperty conditionProperty()
  {
    return conditionProp;
  }

  public StringProperty colorProperty()
  {
    return colorProp;
  }

  public StringProperty priceProperty()
  {
    return priceProp;
  }

  public StringProperty speedProperty()
  {
    return speedProp;
  }

  public StringProperty rangeProperty()
  {
    return rangeProp;
  }

  public StringProperty bikeTypeProperty()
  {
    return bikeTypeProp;
  }

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

  public StringProperty profileTextRedirectProperty()
  {
    return profileTextRedirectProp;
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public BooleanProperty enableAddButtonProperty()
  {
    return disableAddButtonProp;
  }

  public void setProfileInitials()
  {
    String firstname = AppState.getCurrentUser().firstName();
    String lastname = AppState.getCurrentUser().lastName();
    profileTextRedirectProp.set("" + firstname.charAt(0) + lastname.charAt(0));
  }

  private void updateAddButtonState(Observable observable)
  {
    boolean shouldDisable =
        StringUtils.isNullOrEmpty(typeProp.get()) || StringUtils.isNullOrEmpty(
            brandProp.get()) || StringUtils.isNullOrEmpty(modelProp.get())
            || StringUtils.isNullOrEmpty(conditionProp.get())
            || StringUtils.isNullOrEmpty(colorProp.get())
            || StringUtils.isNullOrEmpty(String.valueOf(priceProp.get()));

    // Additional fields depending on the selected type
    if ("scooter".equals(typeProp.get()))
    {
      shouldDisable =
          shouldDisable || StringUtils.isNullOrEmpty(speedProp.get())
              || StringUtils.isNullOrEmpty(rangeProp.get());
    }
    else if ("bike".equals(typeProp.get()))
    {
      shouldDisable =
          shouldDisable || StringUtils.isNullOrEmpty(bikeTypeProp.get());
    }
    else if ("e-bike".equals(typeProp.get()))
    {
      shouldDisable =
          shouldDisable || StringUtils.isNullOrEmpty(speedProp.get())
              || StringUtils.isNullOrEmpty(rangeProp.get())
              || StringUtils.isNullOrEmpty(bikeTypeProp.get());
    }
    disableAddButtonProp.set(shouldDisable);
  }
}
