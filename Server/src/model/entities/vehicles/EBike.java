package model.entities.vehicles;

public class EBike extends Vehicle
{
  private int maxSpeed, oneChargeRange;
  String bikeType;

  public EBike(String type, String brand, String model, String condition, String color, double pricePerDay, int maxSpeed, int oneChargeRange, String bikeType){
    super(type, brand, model, condition, color, pricePerDay);

    this.maxSpeed = maxSpeed;
    this.oneChargeRange = oneChargeRange;
    this.bikeType = bikeType;
  }

  public int getMaxSpeed()
  {
    return maxSpeed;
  }

  public int getOneChargeRange()
  {
    return oneChargeRange;
  }

  public String getBikeType()
  {
    return bikeType;
  }
}
