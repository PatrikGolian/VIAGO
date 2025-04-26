package dtos.vehicle;

public record AddNewScooterRequest( int id,
                                    String type,
                                    String brand,
                                    String model,
                                    String condition,
                                    String color,
                                    double pricePerDay,
                                    int maxSpeed,
                                    int oneChargeRange) implements AddNewVehicleRequest
{
  @Override public String toString()
  {
    return "AddNewScooterRequest{" + "type='" + type + '\'' + ", brand='"
        + brand + '\'' + ", model='" + model + '\'' + ", condition='"
        + condition + '\'' + ", color='" + color + '\'' + ", pricePerDay="
        + pricePerDay + ", maxSpeed=" + maxSpeed + ", oneChargeRange="
        + oneChargeRange + '}';
  }
}
