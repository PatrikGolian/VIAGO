package dtos.vehicle;

public record AddNewScooterDto(int id,
                               String type,
                               String brand,
                               String model,
                               String condition,
                               String color,
                               double pricePerDay,
                               int maxSpeed,
                               int oneChargeRange,
                               String ownerEmail,
                               String state) implements VehicleDataDto
{
  @Override public String toString()
  {
    return "AddNewScooterDto{" + "id=" + id + ", type='" + type + '\''
        + ", brand='" + brand + '\'' + ", model='" + model + '\''
        + ", condition='" + condition + '\'' + ", color='" + color + '\''
        + ", pricePerDay=" + pricePerDay + ", maxSpeed=" + maxSpeed
        + ", oneChargeRange=" + oneChargeRange + ", ownerEmail='" + ownerEmail
        + '\'' + ", state='" + state + '\'' + '}';
  }
}
