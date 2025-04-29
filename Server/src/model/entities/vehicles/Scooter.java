package model.entities.vehicles;

public class Scooter extends Vehicle
{
  private int maxSpeed, oneChargeRange;

  public Scooter(int id,String type, String brand, String model, String condition, String color, double pricePerDay, int maxSpeed, int oneChargeRange){
    super(id,type, brand, model, condition, color, pricePerDay);

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
