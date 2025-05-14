package dtos.vehicle;

public record BikeChangeRequest(int id, String type, double pricePerDay) implements VehicleChangeRequest
{
  @Override public String toString()
  {
    return "BikeChangeRequest{" + "id=" + id + ", type='" + type + '\''
        + ", pricePerDay=" + pricePerDay + '}';
  }
}
