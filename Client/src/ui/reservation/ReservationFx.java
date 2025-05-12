package ui.reservation;

import dtos.reservation.ReservationDisplayDto;
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
  private final StringProperty typeProp = new SimpleStringProperty();
  private final StringProperty startDateProp = new SimpleStringProperty();
  private final StringProperty endDateProp = new SimpleStringProperty();
  private final DoubleProperty priceProp = new SimpleDoubleProperty();
  private final StringProperty ownerEmailProp = new SimpleStringProperty();


  public ReservationFx(ReservationDisplayDto dto)
  {
    typeProp.set(dto.vehicleType());
    startDateProp.set(dto.startDate().toString());
    endDateProp.set(dto.endDate().toString());
    priceProp.set(dto.price());
    ownerEmailProp.set(dto.ownerEmail());
  }

  public StringProperty typePropProperty()
  {
    return typeProp;
  }

  public StringProperty startDaterPropProperty()
  {
    return startDateProp;
  }

  public StringProperty endDatePropProperty()
  {
    return endDateProp;
  }

  public DoubleProperty pricePropProperty()
  {
    return priceProp;
  }

  public StringProperty ownerEmailPropProperty()
  {
    return ownerEmailProp;
  }
}
