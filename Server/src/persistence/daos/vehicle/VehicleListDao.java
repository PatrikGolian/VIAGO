package persistence.daos.vehicle;

import model.entities.vehicles.Vehicle;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VehicleListDao implements VehicleDao
{

  private final static List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(

  ));

  public VehicleListDao()
  {

  }

  @Override
  public void add(Vehicle vehicle)
  {
    vehicles.add(vehicle);
  }


  /**
   * Find a User entity by a given email.
   * Return null, if no matching user was found.
   * I could have thrown an exception instead, it might be simpler.
   *
   * @param type
   * @return
   */
  @Override
  public ArrayList<Vehicle> getByType(String type)
  {
    ArrayList<Vehicle> returns = null;
    for (Vehicle vehicle : vehicles)
    {
      if (type.equals(vehicle.getType()))
      {
        returns.add(vehicle);
      }
    }
    return returns;
  }

  @Override
  public void delete(Vehicle vehicle)
  {
    vehicles.remove(vehicle);
    // Will implement later. Just remove from list.
  }

  @Override
  public void save(Vehicle vehicle, Vehicle oldVehicle)
  {
    vehicles.remove(oldVehicle);
    System.out.println("updated: " + vehicle);
    vehicles.add(vehicle);
  }

}
