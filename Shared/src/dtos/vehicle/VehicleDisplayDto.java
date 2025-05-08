package dtos.vehicle;

import java.io.Serializable;

public interface VehicleDisplayDto extends Serializable
{
  int id();
  String type(); // "bike", "e-bike", "scooter"
  String brand();
  String model();
  double pricePerDay();
  String state();
  String condition();
  String color();
  String ownerEmail();
}
