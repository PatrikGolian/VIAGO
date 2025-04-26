package model.entities.vehicles;

public class Bike extends Vehicle
{
  private String bikeType;
  public Bike(String type, String brand, String model, String condition, String color, double pricePerDay, String bikeType){
    super(type, brand, model, condition, color, pricePerDay);
    this.bikeType = bikeType;
  }


  public String getBikeType()
  {
    return bikeType;
  }
}
