package dtos.vehicle;

public record DeleteScooterRequest(int id,
                                   String type,
                                   String brand,
                                   String model,
                                   String condition,
                                   String color,
                                   double pricePerDay,
                                   int maxSpeed,
                                   int oneChargeRange,
                                   String ownerEmail,
                                   String state) implements DeleteVehicleRequest
{
  @Override public String toString()
  {
    return "AddNewScooterRequest{" + "id=" + id + ", type='" + type + '\''
        + ", brand='" + brand + '\'' + ", model='" + model + '\''
        + ", condition='" + condition + '\'' + ", color='" + color + '\''
        + ", pricePerDay=" + pricePerDay + ", maxSpeed=" + maxSpeed
        + ", oneChargeRange=" + oneChargeRange + ", ownerEmail='" + ownerEmail
        + '\'' + ", state='" + state + '\'' + '}';
  }
}
