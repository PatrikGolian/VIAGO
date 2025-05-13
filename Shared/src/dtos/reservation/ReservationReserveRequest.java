package dtos.reservation;

import java.io.Serializable;

public record ReservationReserveRequest(String reservedByEmail) implements
    Serializable
{
  public String toString()
  {
    return "ReservationReserveRequest{" + "reservedByEmail='" + reservedByEmail + "}'";
  }
}
