package model.entities.vehicles;

public class Scooter extends Vehicle
{
  private int maxSpeed, oneChargeRange;

  public Scooter(String type, String brand, String model, String condition, String color, double pricePerDay, int maxSpeed, int oneChargeRange){
    super(type, brand, model, condition, color, pricePerDay);

    this.maxSpeed = maxSpeed;
    this.oneChargeRange = oneChargeRange;
  }

  public int getMaxSpeed()
  {
    return maxSpeed;
  }

  public int getOneChargeRange()
  {
    return oneChargeRange;
  }
}
