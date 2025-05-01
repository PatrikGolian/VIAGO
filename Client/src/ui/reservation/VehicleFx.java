package ui.reservation;

import dtos.vehicle.VehicleDisplayDto;
import javafx.beans.property.*;

public class VehicleFx
{
  private final StringProperty typeProp = new SimpleStringProperty();
  private final StringProperty brandProp = new SimpleStringProperty();
  private final StringProperty modelProp = new SimpleStringProperty();
  private final DoubleProperty pricePerDayProp = new SimpleDoubleProperty();
  private final StringProperty stateProp = new SimpleStringProperty();

  public VehicleFx(VehicleDisplayDto vehicle)
  {
  typeProp.set(vehicle.type());
  brandProp.set(vehicle.brand());
  modelProp.set(vehicle.model());
  pricePerDayProp.set(vehicle.pricePerDay());
  stateProp.set(vehicle.state());
  }

  public String getTypeProp()
  {
    return typeProp.get();
  }

  public StringProperty typePropProperty()
  {
    return typeProp;
  }

  public String getBrandProp()
  {
    return brandProp.get();
  }

  public StringProperty brandPropProperty()
  {
    return brandProp;
  }

  public String getModelProp()
  {
    return modelProp.get();
  }

  public StringProperty modelPropProperty()
  {
    return modelProp;
  }

  public double getPricePerDayProp()
  {
    return pricePerDayProp.get();
  }

  public DoubleProperty pricePerDayPropProperty()
  {
    return pricePerDayProp;
  }

  public String getStateProp()
  {
    return stateProp.get();
  }

  public StringProperty statePropProperty()
  {
    return stateProp;
  }
}
