package services.myvehicles;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationReserveRequest;
import dtos.vehicle.*;
import model.entities.reservation.Reservation;
import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;
import persistence.reservation.ReservationDao;
import persistence.vehicle.VehicleDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyVehiclesServiceImpl implements MyVehiclesService
{
  private final VehicleDao vehicleDao;

  public MyVehiclesServiceImpl(VehicleDao vehicleDao)
  {
    this.vehicleDao = vehicleDao;
  }

  @Override public List<VehicleDisplayDto> getVehiclesOverview(
      VehicleOwnerRequest ownerEmail) throws SQLException
  {
    List<Vehicle> vehicles = vehicleDao.getByOwnerEmail(ownerEmail.ownerEmail());
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

  @Override public void delete()
  {

  }
}
