package model.entities.vehicles.type;

import model.entities.vehicles.Vehicle;

public class EBike extends Vehicle
{
  public EBike(int type, String brand, String model, int condition, String color, double pricePerDay){
    super(type, brand, model, condition, color, pricePerDay);
  }
}
