package dtos.vehicle;

public record EBikeChangeRequest(int id, String type, double pricePerDay) implements VehicleChangeRequest
{
  @Override public String toString()
  {
    return "EBikeChangeRequest{" + "id=" + id + ", type='" + type + '\''
        + ", pricePerDay=" + pricePerDay + '}';
  }
}
