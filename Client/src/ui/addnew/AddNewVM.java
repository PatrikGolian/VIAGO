package ui.addnew;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.addnew.AddNewClient;
import utils.StringUtils;

import javax.print.DocFlavor;

public class AddNewVM
{
  private final StringProperty typeProp = new SimpleStringProperty();
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
  private final BooleanProperty disableAddButtonProp = new SimpleBooleanProperty(true);

  private final AddNewClient addNewService;

  public AddNewVM(AddNewClient addNewService)
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
    // logic for adding to the system ig

    messageProp.set(""); // clear potential existing message

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
    return  speedFieldVisibility;
  }

  public BooleanProperty getRangeLabelVisibility()
  {
    return  rangeLabelVisibility;
  }

  public BooleanProperty getRangeFieldVisibility()
  {
    return  rangeFieldVisibility;
  }

  public BooleanProperty getBikeTypeLabelVisibility()
  {
    return  bikeTypeLabelVisibility;
  }

  public BooleanProperty getBikeTypeFieldVisibility()
  {
    return  bikeTypeFieldVisibility;
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public BooleanProperty enableAddButtonProperty()
  {
    return disableAddButtonProp;
  }

  private void updateAddButtonState(Observable observable)
  {
    boolean shouldDisable =
        StringUtils.isNullOrEmpty(typeProp.get()) ||
            StringUtils.isNullOrEmpty(brandProp.get()) ||
            StringUtils.isNullOrEmpty(modelProp.get()) ||
            StringUtils.isNullOrEmpty(conditionProp.get()) ||
            StringUtils.isNullOrEmpty(colorProp.get()) ||
            StringUtils.isNullOrEmpty(priceProp.get());

    // Additional fields depending on the selected type
    if ("scooter".equals(typeProp.get()))
    {
      shouldDisable = shouldDisable ||
          StringUtils.isNullOrEmpty(speedProp.get()) ||
          StringUtils.isNullOrEmpty(rangeProp.get());
    }
    else if ("bike".equals(typeProp.get()))
    {
      shouldDisable = shouldDisable ||
          StringUtils.isNullOrEmpty(bikeTypeProp.get());
    }
    else if ("e-bike".equals(typeProp.get()))
    {
      shouldDisable = shouldDisable ||
          StringUtils.isNullOrEmpty(speedProp.get()) ||
          StringUtils.isNullOrEmpty(rangeProp.get()) ||
          StringUtils.isNullOrEmpty(bikeTypeProp.get());
    }
    disableAddButtonProp.set(shouldDisable);
  }
}
