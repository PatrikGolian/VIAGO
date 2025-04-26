package model.entities.vehicles;

public class Vehicle
{
  private int type, condition; //type[bike,e-bike,scooter], condition[used,good,likeNew]
  private double pricePerDay;
  private String brand, model, color;
  private String[] types = {"bike", "e-bike", "scooter"}, conditions = {"used","good","likeNew"};

  public Vehicle(int type, String brand, String model, int condition, String color, double pricePerDay)
  {
    this.type = type;
    this.condition = condition;
    this.pricePerDay = pricePerDay;
    this.brand = brand;
    this.model = model;
    this.color = color;
  }
}
