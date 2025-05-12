package dtos.reservation;

import java.io.Serializable;

public record ReservationRequestByIdType(int vehicleId, String vehicleType) implements
    Serializable
{
  @Override public String toString()
  {
    return "ReservationRequestByIdType{" + "vehicleId=" + vehicleId
        + ", vehicleType='" + vehicleType + '\'' + '}';
  }
}
