package model.entities.vehicles;

public class Vehicle
{
  private String ownerEmail;
  private int id;
  private String type, condition; //type[bike,e-bike,scooter], condition[used,good,likeNew]
  private double pricePerDay;
  private String brand, model, color;
  private String state;

  public Vehicle(int id, String type, String brand, String model,
      String condition, String color, double pricePerDay, String ownerEmail,
      String state)
  {
    this.id = id;
    this.type = type;
    this.condition = condition;
    this.pricePerDay = pricePerDay;
    this.brand = brand;
    this.model = model;
    this.color = color;
    this.ownerEmail = ownerEmail;
    this.state = state;
  }

  public Vehicle(Vehicle vehicle)
  {
    this.id = vehicle.id;
    this.type = vehicle.type;
    this.condition = vehicle.condition;
    this.pricePerDay = vehicle.pricePerDay;
    this.brand = vehicle.brand;
    this.model = vehicle.model;
    this.color = vehicle.color;
    this.ownerEmail = vehicle.ownerEmail;
    this.state = vehicle.state;
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

  public String getOwnerEmail()
  {
    return ownerEmail;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

}
