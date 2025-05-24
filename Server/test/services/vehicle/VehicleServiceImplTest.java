package services.vehicle;

import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;
import dtos.vehicle.VehicleDisplayDto;
import dtos.vehicle.BikeDisplayDto;
import dtos.vehicle.EBikeDisplayDto;
import dtos.vehicle.ScooterDisplayDto;
import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;
import model.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.vehicle.VehicleDao;
import dtos.vehicle.AddNewVehicleRequest; // needed for unknown request type test
import services.vehicle.VehicleServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceImplTest {
  private VehicleServiceImpl service;
  private StubVehicleDao stubDao;

  @BeforeEach
  void setUp() {
    stubDao = new StubVehicleDao();
    service = new VehicleServiceImpl(stubDao);
  }

  // Z: null addNew request -> NullPointerException
  @Test
  void addNew_nullRequest_throwsNPE() {
    assertThrows(NullPointerException.class, () -> service.addNew(null));
  }

  // O: valid Bike
  @Test
  void addNew_validBike_addsBike() throws SQLException {
    AddNewBikeRequest req = new AddNewBikeRequest(1, "bike", "BrandA", "ModelA", "new", "Red", 10.0, "Mountain", "111111@via.dk", "Available");
    service.addNew(req);
    assertEquals(1, stubDao.added.size());
    Vehicle v = stubDao.added.get(0);
    assertTrue(v instanceof Bike);
    assertEquals("BrandA", ((Bike)v).getBrand());
  }

  // O: valid EBike
  @Test
  void addNew_validEBike_addsEBike() throws SQLException {
    AddNewEBikeRequest req = new AddNewEBikeRequest(2, "e-bike", "BrandB", "ModelB", "new", "Blue", 20.0, 25, 100, "City", "111111@via.dk", "Available");
    service.addNew(req);
    assertEquals(1, stubDao.added.size());
    assertTrue(stubDao.added.get(0) instanceof EBike);
  }

  // O: valid Scooter
  @Test
  void addNew_validScooter_addsScooter() throws SQLException {
    AddNewScooterRequest req = new AddNewScooterRequest(3, "scooter", "BrandC", "ModelC", "used", "Green", 15.0, 30, 80, "111111@via.dk", "Available");
    service.addNew(req);
    assertEquals(1, stubDao.added.size());
    assertTrue(stubDao.added.get(0) instanceof Scooter);
  }

  // B: invalid type
  @Test
  void addNew_invalidType_throwsValidationException() {
    AddNewBikeRequest req = new AddNewBikeRequest(1, "car", "BrandA", "ModelA", "new", "Red", 10.0, "Mountain", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // B: invalid brand
  @Test
  void addNew_invalidBrand_throwsValidationException() {
    AddNewBikeRequest req = new AddNewBikeRequest(1, "bike", "Brand1", "ModelA", "new", "Red", 10.0, "Mountain", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // B: empty model
  @Test
  void addNew_emptyModel_throwsValidationException() {
    AddNewBikeRequest req = new AddNewBikeRequest(1, "bike", "BrandA", "", "new", "Red", 10.0, "Mountain", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // B: invalid condition
  @Test
  void addNew_invalidCondition_throwsValidationException() {
    AddNewBikeRequest req = new AddNewBikeRequest(1, "bike", "BrandA", "ModelA", "old", "Red", 10.0, "Mountain", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // B: invalid color
  @Test
  void addNew_invalidColor_throwsValidationException() {
    AddNewBikeRequest req = new AddNewBikeRequest(1, "bike", "BrandA", "ModelA", "new", "Red1", 10.0, "Mountain", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // B: non-positive price
  @Test
  void addNew_zeroPrice_throwsValidationException() {
    AddNewBikeRequest req = new AddNewBikeRequest(1, "bike", "BrandA", "ModelA", "new", "Red", 0.0, "Mountain", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // B: invalid bikeType
  @Test
  void addNew_invalidBikeType_throwsValidationException() {
    AddNewBikeRequest req = new AddNewBikeRequest(1, "bike", "BrandA", "ModelA", "new", "Red", 10.0, "123", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // B: non-positive maxSpeed
  @Test
  void addNew_zeroMaxSpeedEBike_throwsValidationException() {
    AddNewEBikeRequest req = new AddNewEBikeRequest(2, "e-bike", "BrandB", "ModelB", "new", "Blue", 20.0, 0, 100, "City", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // B: non-positive oneChargeRange
  @Test
  void addNew_zeroRangeEBike_throwsValidationException() {
    AddNewEBikeRequest req = new AddNewEBikeRequest(2, "e-bike", "BrandB", "ModelB", "new", "Blue", 20.0, 25, 0, "City", "111111@via.dk", "Available");
    assertThrows(ValidationException.class, () -> service.addNew(req));
  }

  // E: dao add throws SQLException
  @Test
  void addNew_daoThrowsSQLException() {
    stubDao.failAdd = true;
    AddNewBikeRequest req = new AddNewBikeRequest(1, "bike", "BrandA", "ModelA", "new", "Red", 10.0, "Mountain", "111111@via.dk", "Available");
    assertThrows(SQLException.class, () -> service.addNew(req));
  }

  // Unknown request type
  @Test
  void addNew_unknownRequestType_throwsIllegalArgumentException() {
    // Use anonymous implementation of AddNewVehicleRequest
    assertThrows(IllegalArgumentException.class,
        () -> service.addNew(new AddNewVehicleRequest() {}));
  }

  // Zero overview
  @Test
  void getVehiclesOverview_zero_returnsEmpty() throws SQLException {
    stubDao.toReturn = Collections.emptyList();
    List<VehicleDisplayDto> list = service.getVehiclesOverview();
    assertTrue(list.isEmpty());
  }

  // Many overview
  @Test
  void getVehiclesOverview_many_returnsAll() throws SQLException {
    Bike b = new Bike(1, "bike", "BrandA", "ModelA", "new", "Red", 10.0, "Mountain", "111111@via.dk", "Available");
    EBike e = new EBike(2, "e-bike", "BrandB", "ModelB", "new", "Blue", 20.0, "City", 25, 100, "111111@via.dk", "Available");
    Scooter s = new Scooter(3, "scooter", "BrandC", "ModelC", "new", "Green", 15.0, 30, 80, "111111@via.dk", "Available");
    stubDao.toReturn = List.of(b,e,s);
    List<VehicleDisplayDto> list = service.getVehiclesOverview();
    assertEquals(3, list.size());
    assertTrue(list.get(0) instanceof BikeDisplayDto);
    assertTrue(list.get(1) instanceof EBikeDisplayDto);
    assertTrue(list.get(2) instanceof ScooterDisplayDto);
  }

  // Unknown subtype overview
  @Test
  void getVehiclesOverview_unknownSubtype_throwsIllegalStateException() throws SQLException {
    // Vehicle type that ServiceImpl cannot handle
    stubDao.toReturn = List.of(
        new Vehicle(1, "unknown", "BrandX", "ModelX", "new", "Black", 1.0, "111111@via.dk", "Available")
    );
    assertThrows(IllegalStateException.class,
        () -> service.getVehiclesOverview());
  }

  // Stub implementation
  private static class StubVehicleDao implements VehicleDao {
    private List<Vehicle> toReturn = Collections.emptyList();
    private final List<Vehicle> added = new ArrayList<>();
    boolean failAdd = false;

    @Override
    public void add(Vehicle vehicle) throws SQLException {
      if (failAdd) throw new SQLException("add failure");
      added.add(vehicle);
    }

    @Override
    public ArrayList<Vehicle> getAll() throws SQLException {
      return new ArrayList<>(toReturn);
    }

    @Override
    public Vehicle getByIdAndType(int id, String vehicleType) throws SQLException {
      return null;
    }

    @Override
    public ArrayList<Vehicle> getByOwnerEmail(String ownerEmail) throws SQLException {
      return new ArrayList<>(toReturn);
    }

    @Override
    public void delete(Vehicle vehicle) throws SQLException {
    }

    @Override
    public void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException {
    }
  }
}
