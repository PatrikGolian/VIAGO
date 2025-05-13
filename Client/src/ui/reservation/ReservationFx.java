package ui.reservation;

import dtos.reservation.ReservationDto;
import javafx.beans.property.*;

import model.Date;

public class ReservationFx
{
  private final StringProperty typeProp = new SimpleStringProperty();
  private final StringProperty startDateProp = new SimpleStringProperty();
  private final StringProperty endDateProp = new SimpleStringProperty();
  private final DoubleProperty priceProp = new SimpleDoubleProperty();
  private final StringProperty ownerEmailProp = new SimpleStringProperty();
  private final StringProperty reservedByProp = new SimpleStringProperty();
  private final IntegerProperty idProp = new SimpleIntegerProperty();


  public ReservationFx(ReservationDto dto)
  {
    typeProp.set(dto.vehicleType());
    startDateProp.set(dto.startDate().toString());
    endDateProp.set(dto.endDate().toString());
    priceProp.set(dto.price());
    ownerEmailProp.set(dto.ownerEmail());
    idProp.set(dto.vehicleId());
  }

  public StringProperty typePropProperty()
  {
    return typeProp;
  }

  public StringProperty startDatePropProperty()
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

  public IntegerProperty idPropProperty()
  {
    return idProp;
  }

  public StringProperty ownerEmailPropProperty()
  {
    return ownerEmailProp;
  }
  public StringProperty getReservedByProp()
  {
    return reservedByProp;
  }
}
