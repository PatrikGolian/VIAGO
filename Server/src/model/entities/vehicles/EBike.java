package model.entities.vehicles;

public class EBike extends Vehicle
{
  private int maxSpeed, oneChargeRange;
  String bikeType;

  public EBike(int id,String type, String brand, String model, String condition, String color, double pricePerDay, String bikeType, int maxSpeed, int oneChargeRange, String ownerEmail, String state){
    super(id, type, brand, model, condition, color, pricePerDay, ownerEmail, state);

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
