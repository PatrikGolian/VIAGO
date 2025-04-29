package dtos.vehicle;

public record AddNewEBikeRequest( int id,
                                  String type,
                                  String brand,
                                  String model,
                                  String condition,
                                  String color,
                                  double pricePerDay,
                                  int maxSpeed,
                                  int oneChargeRange,
                                  String bikeType) implements AddNewVehicleRequest
{
  @Override
  public String toString()
  {
    return "AddNewEBikeRequest{" + "type='" + type + '\'' + ", brand='" + brand
        + '\'' + ", model='" + model + '\'' + ", condition='" + condition + '\''
        + ", color='" + color + '\'' + ", pricePerDay=" + pricePerDay
        + ", maxSpeed=" + maxSpeed + ", oneChargeRange=" + oneChargeRange
        + ", bikeType='" + bikeType + '\'' + '}';
  }
}
