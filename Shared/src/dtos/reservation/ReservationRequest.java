package dtos.reservation;

import model.Date;

import java.io.Serializable;

public record ReservationRequest(int vehicleId,
                                 String vehicleType,
                                 String ownerEmail,
                                 String reservedByEmail,
                                 Date startDate,
                                 Date endDate,
                                 double price) implements Serializable
{
  @Override public String toString()
  {
    return "ReservationRequest{" + "vehicleId=" + vehicleId + ", vehicleType='"
        + vehicleType + '\'' + ", ownerEmail='" + ownerEmail + '\''
        + ", reservedByEmail='" + reservedByEmail + '\'' + ", startDate="
        + startDate + ", endDate=" + endDate + ", price=" + price + '}';
  }
}
