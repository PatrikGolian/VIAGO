package dtos.vehicle;

import java.io.Serializable;

public record VehicleOwnerRequest(String ownerEmail) implements Serializable
{
  @Override public String toString()
  {
    return "VehicleOwnerRequest{" + "ownerEmail='" + ownerEmail + '\'' + '}';
  }
}
