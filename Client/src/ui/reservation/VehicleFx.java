package ui.reservation;

import dtos.vehicle.*;
import javafx.beans.property.*;

public class VehicleFx
{
  // Related to table view
  private final StringProperty typeProp = new SimpleStringProperty();
  private final StringProperty brandProp = new SimpleStringProperty();
  private final StringProperty modelProp = new SimpleStringProperty();
  private final DoubleProperty pricePerDayProp = new SimpleDoubleProperty();
  private final StringProperty stateProp = new SimpleStringProperty();

  // Related to fields
  private final StringProperty conditionProp = new SimpleStringProperty();
  private final StringProperty colorProp = new SimpleStringProperty();
  private final StringProperty ownerProp = new SimpleStringProperty();
  private final StringProperty bikeTypeProp = new SimpleStringProperty();
  private final StringProperty maxSpeedProp = new SimpleStringProperty();
  private final StringProperty rangeProp = new SimpleStringProperty();

  // Related to reservation
  private final IntegerProperty idProp = new SimpleIntegerProperty();

  public VehicleFx(VehicleDisplayDto vehicle)
  {
    idProp.set(vehicle.id());
    typeProp.set(vehicle.type());
    brandProp.set(vehicle.brand());
    modelProp.set(vehicle.model());
    pricePerDayProp.set(vehicle.pricePerDay());
    stateProp.set(vehicle.state());
    conditionProp.set(vehicle.condition());
    colorProp.set(vehicle.color());
    ownerProp.set(vehicle.ownerEmail());

    switch (vehicle.type())
    {
      case "bike" ->
      {
        bikeTypeProp.set(((BikeDisplayDto) vehicle).bikeType());
      }
      case "e-bike" ->
      {
        EBikeDisplayDto ebike = (EBikeDisplayDto) vehicle;
        bikeTypeProp.set(ebike.bikeType());
        maxSpeedProp.set(String.valueOf(ebike.maxSpeed()));
        rangeProp.set(String.valueOf(ebike.oneChargeRange()));
      }
      case "scooter" ->
      {
        ScooterDisplayDto scooter = (ScooterDisplayDto) vehicle;
        maxSpeedProp.set(String.valueOf(scooter.maxSpeed()));
        rangeProp.set(String.valueOf(scooter.oneChargeRange()));
      }
    }
  }

  // Table view related
  public StringProperty typePropProperty()
  {
    return typeProp;
  }

  public StringProperty brandPropProperty()
  {
    return brandProp;
  }

  public StringProperty modelPropProperty()
  {
    return modelProp;
  }

  public DoubleProperty pricePerDayPropProperty()
  {
    return pricePerDayProp;
  }

  public StringProperty statePropProperty()
  {
    return stateProp;
  }

  // Field related
  public StringProperty conditionPropProperty()
  {
    return conditionProp;
  }

  public StringProperty colorPropProperty()
  {
    return colorProp;
  }

  public StringProperty ownerPropProperty()
  {
    return ownerProp;
  }

  public StringProperty bikeTypePropProperty()
  {
    return bikeTypeProp;
  }

  public StringProperty maxSpeedPropProperty()
  {
    return maxSpeedProp;
  }

  public StringProperty rangeProperty()
  {
    return rangeProp;
  }

  // Reservation related
  public IntegerProperty idPropProperty()
  {
    return idProp;
  }
}
