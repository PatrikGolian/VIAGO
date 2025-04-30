package dtos.vehicle;

public record AddNewBikeRequest(int id,
                                String type,
                                String brand,
                                String model,
                                String condition,
                                String color,
                                double pricePerDay,
                                String bikeType,
                                String ownerEmail,
                                String state) implements AddNewVehicleRequest
{
  @Override public String toString()
  {
    return "AddNewBikeRequest{" + "id=" + id + ", type='" + type + '\''
        + ", brand='" + brand + '\'' + ", model='" + model + '\''
        + ", condition='" + condition + '\'' + ", color='" + color + '\''
        + ", pricePerDay=" + pricePerDay + ", bikeType='" + bikeType + '\''
        + ", ownerEmail='" + ownerEmail + '\'' + ", state='" + state + '\''
        + '}';
  }
}
