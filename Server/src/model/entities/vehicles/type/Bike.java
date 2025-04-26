package model.entities.vehicles.type;
import model.entities.vehicles.Vehicle;

public class Bike extends Vehicle
{
  private String bikeType;
  public Bike(int type, String brand, String model, int condition, String color, double pricePerDay, String bikeType){
    super(type, brand, model, condition, color, pricePerDay);
    this.bikeType = bikeType;
  }
}
