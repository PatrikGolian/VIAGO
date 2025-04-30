package ui.reservation;

import dtos.reservation.ReservationDto;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import model.Date;

public class ReservationFx
{
  private final IntegerProperty vehicleIdProp = new SimpleIntegerProperty();
  private final StringProperty typeProp = new SimpleStringProperty();
  private final StringProperty brandProp = new SimpleStringProperty();
  private final StringProperty modelProp = new SimpleStringProperty();
  private final IntegerProperty pricePerDayProp = new SimpleIntegerProperty();
  private final StringProperty stateProp = new SimpleStringProperty();
  private final StringProperty conditionProp = new SimpleStringProperty();
  private final StringProperty colorProp = new SimpleStringProperty();
  private final StringProperty ownerEmailProp = new SimpleStringProperty();
  private final StringProperty reservedByEmailProp = new SimpleStringProperty();
  private final StringProperty bikeTypeProp = new SimpleStringProperty();
  private final IntegerProperty maxSpeedProp = new SimpleIntegerProperty();
  private final StringProperty rangeProp = new SimpleStringProperty();
  private final ObjectProperty<Date> startDateProp = new SimpleObjectProperty<>();
  private final ObjectProperty<Date> endDateProp = new SimpleObjectProperty<>();
  private final DoubleProperty totalPriceProp = new SimpleDoubleProperty();

  public ReservationFx(ReservationDto dto)
  {
    this.vehicleIdProp.set(dto.vehicleId());
    this.typeProp.set(dto.type());
    this.brandProp.set(dto.brand());
    this.modelProp.set(dto.model());
    this.pricePerDayProp.set(dto.pricePerDay());
    this.stateProp.set(dto.state());
    this.conditionProp.set(dto.condition());
    this.colorProp.set(dto.color());
    this.ownerEmailProp.set(dto.ownerEmail());
    this.reservedByEmailProp.set(dto.reservedByEmail());
    this.bikeTypeProp.set(dto.bikeType());
    this.maxSpeedProp.set(dto.maxSpeed());
    this.rangeProp.set(dto.range());
    this.startDateProp.set(dto.startDate());
    this.endDateProp.set(dto.endDate());
    this.totalPriceProp.set(dto.price());
  }
  public IntegerProperty vehicleIdProperty()
  {
    return vehicleIdProp;
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

  public IntegerProperty pricePerDayProperty()
  {
    return pricePerDayProp;
  }

  public StringProperty stateProperty()
  {
    return stateProp;
  }

  public StringProperty conditionProperty()
  {
    return conditionProp;
  }

  public StringProperty colorProperty()
  {
    return colorProp;
  }

  public StringProperty ownerEmailProperty()
  {
    return ownerEmailProp;
  }

  public StringProperty reservedByEmailProperty()
  {
    return reservedByEmailProp;
  }

  public StringProperty bikeTypeProperty()
  {
    return bikeTypeProp;
  }

  public IntegerProperty maxSpeedProperty()
  {
    return maxSpeedProp;
  }

  public StringProperty rangeProperty()
  {
    return rangeProp;
  }

  public ObjectProperty<Date> startDateProperty()
  {
    return startDateProp;
  }

  public ObjectProperty<Date> endDateProperty()
  {
    return endDateProp;
  }

  public DoubleProperty totalPriceProperty()
  {
    return totalPriceProp;
  }
}
