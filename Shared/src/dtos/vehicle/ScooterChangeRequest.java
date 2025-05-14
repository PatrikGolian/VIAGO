package dtos.vehicle;

public record ScooterChangeRequest(int id, String type, double pricePerDay) implements VehicleChangeRequest
{
  @Override public String toString()
  {
    return "ScooterChangeRequest{" + "id=" + id + ", type='" + type + '\''
        + ", pricePerDay=" + pricePerDay + '}';
  }
}
