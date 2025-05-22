package services.myvehicles;

import dtos.vehicle.VehicleOwnerRequest;
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

class MyVehiclesServiceImplTest {
  private MyVehiclesServiceImpl service;
  private StubVehicleDao stubDao;

  @BeforeEach
  void setUp() {
    stubDao = new StubVehicleDao();
    service = new MyVehiclesServiceImpl(stubDao);
  }

  // Z: zero vehicles overview
  @Test
  void getVehiclesOverview_zero_returnsEmpty() throws SQLException {
    stubDao.toReturn = new ArrayList<>();
    List<VehicleDisplayDto> dtos = service.getVehiclesOverview(new VehicleOwnerRequest("owner@x.com"));
    assertNotNull(dtos);
    assertTrue(dtos.isEmpty());
  }

  // O: many vehicles overview
  @Test
  void getVehiclesOverview_many_returnsDtos() throws SQLException {
    Bike b = new Bike(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "owner@x.com", "Available");
    EBike e = new EBike(2, "e-bike", "BrandB", "ModelB", "cond", "Color", 20.0, "Type", 30, 100, "owner@x.com", "Available");
    Scooter s = new Scooter(3, "scooter", "BrandC", "ModelC", "cond", "Color", 15.0, 40, 120, "owner@x.com", "Available");
    stubDao.toReturn = List.of(b, e, s);

    List<VehicleDisplayDto> dtos = service.getVehiclesOverview(new VehicleOwnerRequest("owner@x.com"));
    assertEquals(3, dtos.size());
    assertTrue(dtos.get(0) instanceof BikeDisplayDto);
    assertTrue(dtos.get(1) instanceof EBikeDisplayDto);
    assertTrue(dtos.get(2) instanceof ScooterDisplayDto);
  }

  // E: unknown subtype overview
  @Test
  void getVehiclesOverview_unknownSubtype_throwsIllegalStateException() throws SQLException {
    stubDao.toReturn = List.of(new Vehicle(99, "unknown", "B", "M", "C", "Color", 5.0, "owner@x.com", "Available"));
    assertThrows(IllegalStateException.class,
        () -> service.getVehiclesOverview(new VehicleOwnerRequest("owner@x.com")));
  }

  // E: DAO throws SQLException
  @Test
  void getVehiclesOverview_daoThrowsSQLException_propagates() {
    stubDao.failGet = true;
    assertThrows(SQLException.class,
        () -> service.getVehiclesOverview(new VehicleOwnerRequest("owner@x.com")));
  }

  // O: delete Bike available
  @Test
  void deleteBike_available_deletesBike() {
    DeleteBikeRequest req = new DeleteBikeRequest(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "owner@x.com", "Available");
    service.delete(req);
    assertEquals(1, stubDao.deleted.size());
    assertTrue(stubDao.deleted.get(0) instanceof Bike);
  }

  // B: delete Bike unavailable
  @Test
  void deleteBike_unavailable_throwsIllegalArgumentException() {
    DeleteBikeRequest req = new DeleteBikeRequest(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "owner@x.com", "RentedOut");
    assertThrows(IllegalArgumentException.class, () -> service.delete(req));
  }

  // E: delete DAO throws SQLException wraps RuntimeException
  @Test
  void deleteBike_daoThrowsSQLException_wrapsRuntimeException() {
    stubDao.failDelete = true;
    DeleteBikeRequest req = new DeleteBikeRequest(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "owner@x.com", "Available");
    RuntimeException ex = assertThrows(RuntimeException.class, () -> service.delete(req));
    assertTrue(ex.getMessage().contains("Failed to delete vehicle"));
  }

  // O: delete EBike available
  @Test
  void deleteEBike_available_deletesEBike() {
    DeleteEBikeRequest req = new DeleteEBikeRequest(
        2,
        "e-bike",
        "BrandB",
        "ModelB",
        "cond",
        "Color",
        20.0,
        30,
        100,
        "Type",
        "owner@x.com",
        "Available"
    );
    service.delete(req);
    assertEquals(1, stubDao.deleted.size());
    assertTrue(stubDao.deleted.get(0) instanceof EBike);
  }

  // O: delete Scooter available
  @Test
  void deleteScooter_available_deletesScooter() {
    DeleteScooterRequest req = new DeleteScooterRequest(3, "scooter", "BrandC", "ModelC", "cond", "Color", 15.0, 40, 120, "owner@x.com", "Available");
    service.delete(req);
    assertEquals(1, stubDao.deleted.size());
    assertTrue(stubDao.deleted.get(0) instanceof Scooter);
  }

  // Z: deleteAll zero vehicles
  @Test
  void deleteAll_zero_doesNothing() {
    stubDao.toReturn = new ArrayList<>();
    assertDoesNotThrow(() -> service.deleteAll(new VehicleOwnerRequest("owner@x.com")));
    assertTrue(stubDao.deleted.isEmpty());
  }

  // O: deleteAll deletes all vehicles
  @Test
  void deleteAll_many_deletesAll() {
    Bike b = new Bike(1, "bike", "BrandA", "ModelA", "cond", "Color", 10.0, "Type", "owner@x.com", "Available");
    EBike e = new EBike(2, "e-bike", "BrandB", "ModelB", "cond", "Color", 20.0, "Type", 30, 100, "owner@x.com", "Available");
    Scooter s = new Scooter(3, "scooter", "BrandC", "ModelC", "cond", "Color", 15.0, 40, 120, "owner@x.com", "Available");
    stubDao.toReturn = List.of(b, e, s);
    service.deleteAll(new VehicleOwnerRequest("owner@x.com"));
    assertEquals(3, stubDao.deleted.size());
  }

  // E: deleteAll unknown subtype
  @Test
  void deleteAll_unknownSubtype_throwsIllegalStateException() {
    stubDao.toReturn = List.of(new Vehicle(99, "unknown", "B", "M", "C", "Color", 5.0, "owner@x.com", "Available"));
    assertThrows(IllegalStateException.class,
        () -> service.deleteAll(new VehicleOwnerRequest("owner@x.com")));
  }

  // E: deleteAll DAO throws SQLException wraps RuntimeException
  @Test
  void deleteAll_daoThrowsSQLException_wrapsRuntimeException() {
    stubDao.failGet = true;
    RuntimeException ex = assertThrows(RuntimeException.class,
        () -> service.deleteAll(new VehicleOwnerRequest("owner@x.com")));
    assertTrue(ex.getMessage().contains("Failed to delete all vehicles"));
  }

  // Stub implementation
  private static class StubVehicleDao implements VehicleDao {
    List<Vehicle> toReturn = new ArrayList<>();
    List<Vehicle> deleted = new ArrayList<>();
    boolean failGet = false;
    boolean failDelete = false;

    @Override
    public ArrayList<Vehicle> getByOwnerEmail(String ownerEmail) throws SQLException {
      if (failGet) throw new SQLException("get failure");
      return new ArrayList<>(toReturn);
    }

    @Override
    public void delete(Vehicle vehicle) throws SQLException {
      if (failDelete) throw new SQLException("delete failure");
      deleted.add(vehicle);
    }

    // unused stubs
    @Override public void add(Vehicle vehicle) throws SQLException {}
    @Override public Vehicle getByIdAndType(int id, String vehicleType) throws SQLException { return null; }
    @Override public ArrayList<Vehicle> getAll() throws SQLException { return new ArrayList<>(); }
    @Override public void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException {}
  }
}
