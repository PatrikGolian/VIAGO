package services.vehicle;

import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;
import dtos.vehicle.AddNewVehicleRequest;
import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;
import persistence.daos.vehicle.VehicleDao;

import java.sql.SQLException;

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
      Bike newBike =  new Bike(
          bikeRequest.type(),
          bikeRequest.brand(),
          bikeRequest.model(),
          bikeRequest.condition(),
          bikeRequest.color(),
          bikeRequest.pricePerDay(),
          bikeRequest.bikeType()
      );
    } else if (request instanceof AddNewEBikeRequest eBikeRequest) {
      EBike newEBike=  new EBike(
          eBikeRequest.type(),
          eBikeRequest.brand(),
          eBikeRequest.model(),
          eBikeRequest.condition(),
          eBikeRequest.color(),
          eBikeRequest.pricePerDay(),
          eBikeRequest.maxSpeed(),
          eBikeRequest.oneChargeRange(),
          eBikeRequest.bikeType()
      );
    } else if (request instanceof AddNewScooterRequest scooterRequest) {
      Scooter newScooter =  new Scooter(
          scooterRequest.type(),
          scooterRequest.brand(),
          scooterRequest.model(),
          scooterRequest.condition(),
          scooterRequest.color(),
          scooterRequest.pricePerDay(),
          scooterRequest.maxSpeed(),
          scooterRequest.oneChargeRange()
      );
    } else {
      throw new IllegalArgumentException("Unknown vehicle request type: " + request.getClass());
    }
  }
}
