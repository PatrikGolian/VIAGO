package dtos.vehicle;

public record DeleteEBikeRequest(int id,
                                 String type,
                                 String brand,
                                 String model,
                                 String condition,
                                 String color,
                                 double pricePerDay,
                                 int maxSpeed,
                                 int oneChargeRange,
                                 String bikeType,
                                 String ownerEmail,
                                 String state) implements DeleteVehicleRequest
{
  @Override public String toString()
  {
    return "AddNewEBikeRequest{" + "id=" + id + ", type='" + type + '\''
        + ", brand='" + brand + '\'' + ", model='" + model + '\''
        + ", condition='" + condition + '\'' + ", color='" + color + '\''
        + ", pricePerDay=" + pricePerDay + ", maxSpeed=" + maxSpeed
        + ", oneChargeRange=" + oneChargeRange + ", bikeType='" + bikeType
        + '\'' + ", ownerEmail='" + ownerEmail + '\'' + ", state='" + state
        + '\'' + '}';
  }
}
