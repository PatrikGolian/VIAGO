package dtos.vehicle;

public record AddNewEBikeDto(int id,
                             String type,
                             String brand,
                             String model,
                             String condition,
                             String color,
                             double pricePerDay,
                             int maxSpeed,
                             int oneChargeRange,
                             String bikeType) implements VehicleDataDto
{
  @Override
  public String toString()
  {
    return "AddNewEBikeDto{" + "type='" + type + '\'' + ", brand='" + brand
        + '\'' + ", model='" + model + '\'' + ", condition='" + condition + '\''
        + ", color='" + color + '\'' + ", pricePerDay=" + pricePerDay
        + ", maxSpeed=" + maxSpeed + ", oneChargeRange=" + oneChargeRange
        + ", bikeType='" + bikeType + '\'' + '}';
  }
}
