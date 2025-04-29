package dtos.vehicle;

public record AddNewBikeRequest(int id,
                                String type,
                                String brand,
                                String model,
                                String condition,
                                String color,
                                double pricePerDay,
                                String bikeType) implements AddNewVehicleRequest
{
  @Override
  public String toString()
  {
    return "AddNewBikeRequest{" + "type='" + type + '\'' + ", brand='" + brand
        + '\'' + ", model='" + model + '\'' + ", condition='" + condition + '\''
        + ", color='" + color + '\'' + ", pricePerDay=" + pricePerDay
        + ", bikeType='" + bikeType + '\'' + '}';
  }
}
