package services.allvehicles;

import dtos.vehicle.*;
import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;
import persistence.vehicle.VehicleDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllVehiclesServiceImpl implements AllVehiclesService
{
  private final VehicleDao vehicleDao;

  public AllVehiclesServiceImpl(VehicleDao vehicleDao)
  {
    this.vehicleDao = vehicleDao;
  }


  @Override public List<VehicleDisplayDto> getVehiclesOverview()
      throws SQLException
  {
    List<Vehicle> vehicles = vehicleDao.getAll();
    List<VehicleDisplayDto> result = new ArrayList<>();

    for (Vehicle vehicle : vehicles)
    {
      if (vehicle instanceof Bike bike)
      {
        result.add(
            new BikeDisplayDto(bike.getId(), bike.getType(), bike.getBrand(),
                bike.getModel(), bike.getPricePerDay(), bike.getState(),
                bike.getCondition(), bike.getColor(), bike.getOwnerEmail(),
                bike.getBikeType()));
      }
      else if (vehicle instanceof EBike ebike)
      {
        result.add(new EBikeDisplayDto(ebike.getId(), ebike.getType(),
            ebike.getBrand(), ebike.getModel(), ebike.getPricePerDay(),
            ebike.getState(), ebike.getCondition(), ebike.getColor(),
            ebike.getOwnerEmail(), ebike.getBikeType(), ebike.getMaxSpeed(),
            ebike.getOneChargeRange()));
      }
      else if (vehicle instanceof Scooter scooter)
      {
        result.add(new ScooterDisplayDto(scooter.getId(), scooter.getType(),
            scooter.getBrand(), scooter.getModel(), scooter.getPricePerDay(),
            scooter.getState(), scooter.getCondition(), scooter.getColor(),
            scooter.getOwnerEmail(), scooter.getMaxSpeed(),
            scooter.getOneChargeRange()));
      }
      else
      {
        throw new IllegalStateException(
            "Unknown vehicle subtype: " + vehicle.getClass().getName());
      }
    }

    return result;
  }

  @Override public void delete(DeleteVehicleRequest request)
  {
    {
      try
      {
        if (request instanceof DeleteBikeRequest
            && ((DeleteBikeRequest) request).state().equals("Available"))
        {
          DeleteBikeRequest bikeRequest = (DeleteBikeRequest) request;
          Bike bike = new Bike(bikeRequest.id(), bikeRequest.type(),
              bikeRequest.brand(), bikeRequest.model(), bikeRequest.condition(),
              bikeRequest.color(), bikeRequest.pricePerDay(),
              bikeRequest.bikeType(), bikeRequest.ownerEmail(),
              bikeRequest.state());
          vehicleDao.delete(bike);
        }
        else if (request instanceof DeleteEBikeRequest
            && ((DeleteEBikeRequest) request).state().equals("Available"))
        {
          DeleteEBikeRequest ebikeRequest = (DeleteEBikeRequest) request;
          EBike ebike = new EBike(ebikeRequest.id(), ebikeRequest.type(),
              ebikeRequest.brand(), ebikeRequest.model(),
              ebikeRequest.condition(), ebikeRequest.color(),
              ebikeRequest.pricePerDay(), ebikeRequest.bikeType(),
              ebikeRequest.maxSpeed(), ebikeRequest.oneChargeRange(),
              ebikeRequest.ownerEmail(), ebikeRequest.state());
          vehicleDao.delete(ebike);
        }
        else if (request instanceof DeleteScooterRequest
            && ((DeleteScooterRequest) request).state().equals("Available"))
        {
          DeleteScooterRequest scooterRequest = (DeleteScooterRequest) request;
          Scooter scooter = new Scooter(scooterRequest.id(),
              scooterRequest.type(), scooterRequest.brand(),
              scooterRequest.model(), scooterRequest.condition(),
              scooterRequest.color(), scooterRequest.pricePerDay(),
              scooterRequest.maxSpeed(), scooterRequest.oneChargeRange(),
              scooterRequest.ownerEmail(), scooterRequest.state());
          vehicleDao.delete(scooter);
        }
        else
        {
          throw new IllegalArgumentException(
              "You can only delete vehicles that are available.");
        }
      }
      catch (SQLException e)
      {
        throw new RuntimeException(
            "Failed to delete vehicle: " + e.getMessage(), e);
      }
    }
  }
}
