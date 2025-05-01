package ui.reservation;

import dtos.reservation.ReservationDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import model.Date;

public class ReservationFx
{
  // Related to reservation table
  private final StringProperty vehicleTypeProp = new SimpleStringProperty();
  private final StringProperty ownerEmailProp = new SimpleStringProperty();
  private final StringProperty reservedByEmailProp = new SimpleStringProperty();
  private final ObjectProperty<Date> startDateProp = new SimpleObjectProperty<>();
  private final ObjectProperty<Date> endDateProp = new SimpleObjectProperty<>();
  private final DoubleProperty priceProp = new SimpleDoubleProperty();

  public ReservationFx(ReservationDto dto)
  {
    // Related to reservation table
    this.vehicleTypeProp.set(dto.vehicleType());
    this.ownerEmailProp.set(dto.ownerEmail());
    this.reservedByEmailProp.set(dto.reservedByEmail());
    this.startDateProp.set(dto.startDate());
    this.endDateProp.set(dto.endDate());
    this.priceProp.set(dto.price());
  }

  public StringProperty typeProperty()
  {
    return vehicleTypeProp;
  }

  public StringProperty ownerEmailProperty()
  {
    return ownerEmailProp;
  }

  public StringProperty reservedByEmailProperty()
  {
    return reservedByEmailProp;
  }

  public ObjectProperty<Date> startDateProperty()
  {
    return startDateProp;
  }

  public ObjectProperty<Date> endDateProperty()
  {
    return endDateProp;
  }

}
