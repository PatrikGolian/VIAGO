package dtos.reservation;

import java.io.Serializable;
import model.Date;

public record ReservationDto(int vehicleId,
                             String vehicleType,
                             String ownerEmail,
                             String reservedByEmail,
                             Date startDate,
                             Date endDate,
                             double price) implements Serializable
{
  @Override public String toString()
  {
    return "ReservationDto{" + "vehicleId=" + vehicleId + ", vehicleType='"
        + vehicleType + '\'' + ", ownerEmail='" + ownerEmail + '\''
        + ", reservedByEmail='" + reservedByEmail + '\'' + ", startDate="
        + startDate + ", endDate=" + endDate + ", price=" + price + '}';
  }
}
