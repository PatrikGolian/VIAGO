package dtos.vehicle;

public record AddNewBikeDto( int id,
                             String type,
                             String brand,
                             String model,
                             String condition,
                             String color,
                             double pricePerDay,
                             String bikeType) implements VehicleDataDto
{
  @Override
  public String toString()
  {
    return "AddNewBikeDto{" + "type='" + type + '\'' + ", brand='" + brand
        + '\'' + ", model='" + model + '\'' + ", condition='" + condition + '\''
        + ", color='" + color + '\'' + ", pricePerDay=" + pricePerDay
        + ", bikeType='" + bikeType + '\'' + '}';
  }
}
