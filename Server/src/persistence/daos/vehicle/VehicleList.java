package persistence.daos.vehicle;

import model.entities.vehicles.Vehicle;

import java.util.List;

public class VehicleList
{
  public List<Vehicle> list;

  public VehicleList(List<Vehicle> vehicles)
  {
    this.list = vehicles;
  }
}

