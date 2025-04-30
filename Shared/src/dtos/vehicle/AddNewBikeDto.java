package dtos.vehicle;

public record AddNewBikeDto( int id,
                             String type,
                             String brand,
                             String model,
                             String condition,
                             String color,
                             double pricePerDay,
                             String bikeType,
                             String ownerEmail,
                             String state) implements VehicleDataDto
{
  @Override public String toString()
  {
    return "AddNewBikeDto{" + "id=" + id + ", type='" + type + '\''
        + ", brand='" + brand + '\'' + ", model='" + model + '\''
        + ", condition='" + condition + '\'' + ", color='" + color + '\''
        + ", pricePerDay=" + pricePerDay + ", bikeType='" + bikeType + '\''
        + ", ownerEmail='" + ownerEmail + '\'' + ", state='" + state + '\''
        + '}';
  }
}
