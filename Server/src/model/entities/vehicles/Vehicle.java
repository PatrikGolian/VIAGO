package model.entities.vehicles;

public class Vehicle
{
  private int id;
  private String type, condition; //type[bike,e-bike,scooter], condition[used,good,likeNew]
  private double pricePerDay;
  private String brand, model, color;

  public Vehicle(String type, String brand, String model, String condition, String color, double pricePerDay)
  {
    this.type = type;
    this.condition = condition;
    this.pricePerDay = pricePerDay;
    this.brand = brand;
    this.model = model;
    this.color = color;
  }

  public String getType()
  {
    return type;
  }

  public String getBrand()
  {
    return brand;
  }

  public String getModel()
  {
    return model;
  }

  public String getCondition()
  {
    return condition;
  }

  public String getColor()
  {
    return color;
  }

  public double getPricePerDay()
  {
    return pricePerDay;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public int getId()
  {
    return id;
  }
}
