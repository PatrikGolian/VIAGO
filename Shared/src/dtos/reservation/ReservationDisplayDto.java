package dtos.reservation;

import model.Date;

import java.io.Serializable;

public record ReservationDisplayDto( String vehicleType,
                                    Date startDate, Date endDate, double price,
                                     String ownerEmail) implements Serializable
{
  @Override public String toString()
  {
    return "ReservationDisplayDto{" + "vehicleType='" + vehicleType + '\''
        + ", startDate=" + startDate + ", endDate=" + endDate + ", price="
        + price + ", ownerEmail='" + ownerEmail + '\'' + '}';
  }
}
