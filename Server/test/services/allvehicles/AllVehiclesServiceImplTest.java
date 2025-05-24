package services.allvehicles;

import dtos.vehicle.DeleteBikeRequest;
import dtos.vehicle.DeleteEBikeRequest;
import dtos.vehicle.DeleteScooterRequest;
import dtos.vehicle.VehicleDisplayDto;
import dtos.vehicle.BikeDisplayDto;
import dtos.vehicle.EBikeDisplayDto;
import dtos.vehicle.ScooterDisplayDto;
import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.vehicle.VehicleDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AllVehiclesServiceImplTest {
  private AllVehiclesServiceImpl service;
  private StubVehicleDao stubDao;

  @BeforeEach
  void setUp() {
    stubDao = new StubVehicleDao();
    service = new AllVehiclesServiceImpl(stubDao);
  }

  @Test
  void getVehiclesOverview_zero_returnsEmpty() throws SQLException {
    stubDao.toReturn = new ArrayList<>();
    List<VehicleDisplayDto> dtos = service.getVehiclesOverview();
    assertNotNull(dtos);
    assertTrue(dtos.isEmpty());
  }

  @Test
  void getVehiclesOverview_many_returnsCorrectDtos() throws SQLException {
    Bike b = new Bike(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "111111@via.dk", "Available");
    EBike e = new EBike(2, "e-bike", "BrandB", "ModelB", "cond", "Color", 20.0, "Type", 30, 100, "111111@via.dk", "Available");
    Scooter s = new Scooter(3, "scooter", "BrandC", "ModelC", "cond", "Color", 15.0, 40, 120, "111111@via.dk", "Available");
    stubDao.toReturn = new ArrayList<>(List.of(b, e, s));

    List<VehicleDisplayDto> dtos = service.getVehiclesOverview();
    assertEquals(3, dtos.size());
    assertTrue(dtos.get(0) instanceof BikeDisplayDto);
    assertTrue(dtos.get(1) instanceof EBikeDisplayDto);
    assertTrue(dtos.get(2) instanceof ScooterDisplayDto);
  }

  @Test
  void getVehiclesOverview_unknownSubtype_throwsIllegalStateException() throws SQLException {
    Vehicle unknown = new Vehicle(99, "unknown", "B", "M", "C", "Color", 5.0, "111111@via.dk", "Available");
    stubDao.toReturn = new ArrayList<>(List.of(unknown));
    assertThrows(IllegalStateException.class, service::getVehiclesOverview);
  }

  @Test
  void getVehiclesOverview_daoThrowsSQLException_propagates() {
    stubDao.failGet = true;
    assertThrows(SQLException.class, service::getVehiclesOverview);
  }

  @Test
  void deleteBike_available_deletesBike() throws SQLException {
    DeleteBikeRequest req = new DeleteBikeRequest(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "111111@via.dk", "Available");
    service.delete(req);
    assertEquals(1, stubDao.deleted.size());
    assertTrue(stubDao.deleted.get(0) instanceof Bike);
  }

  @Test
  void deleteBike_unavailable_throwsIllegalArgumentException() {
    DeleteBikeRequest req = new DeleteBikeRequest(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "111111@via.dk", "RentedOut");
    assertThrows(IllegalArgumentException.class, () -> service.delete(req));
  }

  @Test
  void deleteEBike_available_deletesEBike() throws SQLException {
    DeleteEBikeRequest req = new DeleteEBikeRequest(2, "e-bike", "BrandB", "ModelB", "cond", "Color", 20.0, 100, 30, "Type", "111111@via.dk", "Available");
    service.delete(req);
    assertEquals(1, stubDao.deleted.size());
    assertTrue(stubDao.deleted.get(0) instanceof EBike);
  }

  @Test
  void deleteScooter_available_deletesScooter() throws SQLException {
    DeleteScooterRequest req = new DeleteScooterRequest(3, "scooter", "BrandC", "ModelC", "cond", "Color", 15.0, 40, 120, "111111@via.dk", "Available");
    service.delete(req);
    assertEquals(1, stubDao.deleted.size());
    assertTrue(stubDao.deleted.get(0) instanceof Scooter);
  }

  @Test
  void delete_daoThrowsSQLException_wrapsRuntimeException() {
    stubDao.failDelete = true;
    DeleteBikeRequest req = new DeleteBikeRequest(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "111111@via.dk", "Available");
    RuntimeException ex = assertThrows(RuntimeException.class, () -> service.delete(req));
    assertTrue(ex.getMessage().contains("Failed to delete vehicle"));
  }

  private static class StubVehicleDao implements VehicleDao {
    ArrayList<Vehicle> toReturn = new ArrayList<>();
    ArrayList<Vehicle> deleted = new ArrayList<>();
    boolean failGet = false;
    boolean failDelete = false;

    @Override
    public ArrayList<Vehicle> getAll() throws SQLException {
      if (failGet) throw new SQLException("fail");
      return new ArrayList<>(toReturn);
    }

    @Override
    public void delete(Vehicle vehicle) throws SQLException {
      if (failDelete) throw new SQLException("del fail");
      deleted.add(vehicle);
    }

    @Override public void add(Vehicle vehicle) throws SQLException {}
    @Override public Vehicle getByIdAndType(int id, String vehicleType) throws SQLException { return null; }
    @Override public ArrayList<Vehicle> getByOwnerEmail(String ownerEmail) throws SQLException { return null; }
    @Override public void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException {}
  }
}
