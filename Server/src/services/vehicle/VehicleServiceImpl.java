package services.vehicle;

import dtos.Request;
import dtos.user.ViewUsers;
import dtos.vehicle.*;
import model.entities.User;
import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;
import model.exceptions.ValidationException;
import persistence.daos.vehicle.VehicleDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImpl implements VehicleService
{

  private final VehicleDao vehicleDao;

  public VehicleServiceImpl(VehicleDao vehicleDao)
  {
    this.vehicleDao = vehicleDao;
  }

  @Override public void addNew(AddNewVehicleRequest request) throws SQLException
  {

    if (request instanceof AddNewBikeRequest bikeRequest) {
      typeValidation(bikeRequest.type());
      brandValidation(bikeRequest.brand());
      modelValidation(bikeRequest.model());
      conditionValidation(bikeRequest.condition());
      colorValidation(bikeRequest.color());
      pricePerDayValidation(bikeRequest.pricePerDay());
      bikeTypeValidation(bikeRequest.bikeType());
      
      Bike newBike =  new Bike(
          bikeRequest.id(),
          bikeRequest.type(),
          bikeRequest.brand(),
          bikeRequest.model(),
          bikeRequest.condition(),
          bikeRequest.color(),
          bikeRequest.pricePerDay(),
          bikeRequest.bikeType(),
          bikeRequest.ownerEmail(),
          bikeRequest.state()
      );
      vehicleDao.add(newBike);
    } else if (request instanceof AddNewEBikeRequest eBikeRequest) {
      typeValidation(eBikeRequest.type());
      brandValidation(eBikeRequest.brand());
      modelValidation(eBikeRequest.model());
      conditionValidation(eBikeRequest.condition());
      colorValidation(eBikeRequest.color());
      pricePerDayValidation(eBikeRequest.pricePerDay());
      maxSpeedValidation(eBikeRequest.maxSpeed());
      oneChargeRangeValidation(eBikeRequest.oneChargeRange());
      bikeTypeValidation(eBikeRequest.bikeType());
      
      
      EBike newEBike=  new EBike(
          eBikeRequest.id(),
          eBikeRequest.type(),
          eBikeRequest.brand(),
          eBikeRequest.model(),
          eBikeRequest.condition(),
          eBikeRequest.color(),
          eBikeRequest.pricePerDay(),
          eBikeRequest.bikeType(),
          eBikeRequest.maxSpeed(),
          eBikeRequest.oneChargeRange(),
          eBikeRequest.ownerEmail(),
          eBikeRequest.state()
      );
      vehicleDao.add(newEBike);
    } else if (request instanceof AddNewScooterRequest scooterRequest) {
      typeValidation(scooterRequest.type());
      brandValidation(scooterRequest.brand());
      modelValidation(scooterRequest.model());
      conditionValidation(scooterRequest.condition());
      colorValidation(scooterRequest.color());
      pricePerDayValidation(scooterRequest.pricePerDay());
      maxSpeedValidation(scooterRequest.maxSpeed());
      oneChargeRangeValidation(scooterRequest.oneChargeRange());
      
      Scooter newScooter =  new Scooter(
          scooterRequest.id(),
          scooterRequest.type(),
          scooterRequest.brand(),
          scooterRequest.model(),
          scooterRequest.condition(),
          scooterRequest.color(),
          scooterRequest.pricePerDay(),
          scooterRequest.maxSpeed(),
          scooterRequest.oneChargeRange(),
          scooterRequest.ownerEmail(),
          scooterRequest.state()
      );
      vehicleDao.add(newScooter);
    } else {
      throw new IllegalArgumentException("Unknown vehicle request type: " + request.getClass());
    }

  }
 /* public List<VehicleDisplayDto> getVehiclesOverview()
      throws SQLException
  {
    List<Vehicle> vehicles = vehicleDao.getAll();
    List<VehicleDisplayDto> result = new ArrayList<>();

    for (Vehicle vehicle : vehicles)
    {
      switch ()
      VehicleDisplayDto dto = new VehicleDisplayDto(vehicle.getId(), vehicle.getType(), vehicle.getBrand(), vehicle.getModel(), vehicle.getPricePerDay(),vehicle.getState());
      result.add(dto);
    }
    return result;
  } */
 @Override
 public List<VehicleDisplayDto> getVehiclesOverview() throws SQLException {
   List<Vehicle> vehicles = vehicleDao.getAll();
   List<VehicleDisplayDto> result = new ArrayList<>();

   for (Vehicle vehicle : vehicles) {
     if (vehicle instanceof Bike bike) {
       result.add(new BikeDisplayDto(
           bike.getId(),
           bike.getType(),
           bike.getBrand(),
           bike.getModel(),
           bike.getPricePerDay(),
           bike.getState(),
           bike.getCondition(),
           bike.getColor(),
           bike.getOwnerEmail(),
           bike.getBikeType()
       ));
     } else if (vehicle instanceof EBike ebike) {
       result.add(new EBikeDisplayDto(
           ebike.getId(),
           ebike.getType(),
           ebike.getBrand(),
           ebike.getModel(),
           ebike.getPricePerDay(),
           ebike.getState(),
           ebike.getCondition(),
           ebike.getColor(),
           ebike.getOwnerEmail(),
           ebike.getBikeType(),
           ebike.getMaxSpeed(),
           ebike.getOneChargeRange()
       ));
     } else if (vehicle instanceof Scooter scooter) {
       result.add(new ScooterDisplayDto(
           scooter.getId(),
           scooter.getType(),
           scooter.getBrand(),
           scooter.getModel(),
           scooter.getPricePerDay(),
           scooter.getState(),
           scooter.getCondition(),
           scooter.getColor(),
           scooter.getOwnerEmail(),
           scooter.getMaxSpeed(),
           scooter.getOneChargeRange()
       ));
     } else {
       throw new IllegalStateException("Unknown vehicle subtype: " + vehicle.getClass().getName());
     }
   }

   return result;
 }


  private static void typeValidation(String type)
  {
    if(!(type.equals("bike") || type.equals("e-bike") || type.equals("scooter")) )
    {
      throw new ValidationException("Type has to be either bike, e-bike or scooter");
    }
  }

  private static void brandValidation(String brand)
  {
    if(!brand.matches("[a-zA-Z ]+"))
    {
      throw new ValidationException("Brand can contain only letters");
    }
  }
  private static void modelValidation(String model)
  {
    if(model.isEmpty() || model == null)
    {
      throw new ValidationException("Model cannot be empty");
    }
  }

  private static void conditionValidation(String condition)
  {
    if (!(condition.equals("used")||condition.equals("good")||condition.equals("new")))
    {
      throw new ValidationException("Condition has to be either used, good or new");
    }
  }
  private static void colorValidation(String color)
  {
    if(!color.matches("[a-zA-Z ]+"))
    {
      throw new ValidationException("Color can only contain letters");
    }
  }
  private static void pricePerDayValidation(double price)
  {
    if(price <= 0)
    {
      throw new ValidationException("Price has to be a positive number");
    }
  }

  private static void bikeTypeValidation(String bikeType)
  {
    if (!bikeType.matches("[a-zA-Z ]+"))
    {
      throw new ValidationException("Bike Type can only contain letters");
    }
  }
  private static void maxSpeedValidation(int maxSpeed)
  {
    if(maxSpeed <= 0)
    {
      throw  new ValidationException("Max speed has to be a positive number");
    }
  }
  private static void oneChargeRangeValidation(int oneChargeRange)
  {
    if(oneChargeRange <= 0)
    {
      throw  new ValidationException("Range has to be a positive number");
    }
  }
}
