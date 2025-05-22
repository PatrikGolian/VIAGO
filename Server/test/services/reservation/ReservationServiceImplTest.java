package services.reservation;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationRequestByIdType;
import dtos.reservation.ReservationReserveRequest;
import model.Date;
import model.entities.reservation.Reservation;
import model.entities.vehicles.Vehicle;
import model.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.reservation.ReservationDao;
import persistence.vehicle.VehicleDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceImplTest {
  private ReservationServiceImpl service;
  private StubReservationDao stubResDao;
  private StubVehicleDao stubVehDao;

  @BeforeEach
  void setUp() {
    stubResDao = new StubReservationDao();
    stubVehDao = new StubVehicleDao();
    service = new ReservationServiceImpl(stubResDao, stubVehDao);
  }

  // Z: updateVehicleState with empty reservations
  @Test
  void updateVehicleState_noReservations_doesNothing() {
    stubResDao.toReturn = new ArrayList<>();
    assertDoesNotThrow(() -> service.updateVehicleState());
    assertFalse(stubVehDao.savedCalled);
  }

  // O: reservation starts today -> state set to RentedOut
  @Test
  void updateVehicleState_startToday_rentsOutVehicle() {
    Date today = Date.today();
    Reservation res = new Reservation(1, "bike", "owner@x", "user@x", today, today, 50);
    stubResDao.toReturn = List.of(res);
    Vehicle veh = new Vehicle(res.getVehicleId(), res.getVehicleType(), "brand", "model", "condition", "color", 10.0, "owner@x", "Available");
    stubVehDao.nextVehicle = veh;
    service.updateVehicleState();
    assertTrue(stubVehDao.savedCalled);
    assertEquals("RentedOut", stubVehDao.savedNew.getState());
    assertEquals("Available", stubVehDao.savedOld.getState());
  }

  // O: day after endDate == today -> state set to Available
  @Test
  void updateVehicleState_endYesterday_returnsAvailable() {
    // create yesterday endDate
    Date yesterday = Date.today();
    java.time.LocalDate ld = yesterday.toLocalDate().minusDays(1);
    Date start = new Date(ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear());
    Date end = new Date(start.getDay(), start.getMonth(), start.getYear());
    Reservation res = new Reservation(2, "scooter", "owner@x", "user@x", start, end, 30);
    stubResDao.toReturn = List.of(res);
    Vehicle veh0 = new Vehicle(res.getVehicleId(), res.getVehicleType(), "brand", "model", "condition", "color", 10.0, "owner@x", "RentedOut");
    stubVehDao.nextVehicle = veh0;
    service.updateVehicleState();
    assertTrue(stubVehDao.savedCalled);
    assertEquals("Available", stubVehDao.savedNew.getState());
  }

  // E: validatePeriod end before start -> ValidationException
  @Test
  void addNewReservation_endBeforeStart_throwsValidationException() {
    Date d1 = new Date(10,1,2025);
    Date d2 = new Date(9,1,2025);
    ReservationRequest req = new ReservationRequest(1, "bike", "owner@x", "user@x", d1, d2, 20);
    assertThrows(ValidationException.class, () -> service.addNewReservation(req));
  }

  // O: addNewReservation valid -> calls dao.add
  @Test
  void addNewReservation_valid_callsDaoAdd() {
    Date d = Date.today();
    ReservationRequest req = new ReservationRequest(3, "e-bike", "owner@x", "user@x", d, d, 15);
    service.addNewReservation(req);
    assertTrue(stubResDao.addedCalled);
    assertEquals(3, stubResDao.addedRes.getVehicleId());
  }

  // O: deleteAll calls dao.deleteAll
  @Test
  void deleteAll_invokesDao() {
    ReservationReserveRequest req = new ReservationReserveRequest("user@x");
    service.deleteAll(req);
    assertTrue(stubResDao.deleteAllCalled);
    assertEquals("user@x", stubResDao.deleteAllEmail);
  }

  // O: getReservationsByTypeAndId filters correctly
  @Test
  void getReservationsByTypeAndId_returnsMatching() throws SQLException {
    Reservation r1 = new Reservation(1, "bike", "owner@x", "user@x", Date.today(), Date.today(), 25);
    Reservation r2 = new Reservation(2, "scooter", "owner@x", "user@x", Date.today(), Date.today(), 30);
    stubResDao.byTypeId = List.of(r1, r2);
    ReservationRequestByIdType req = new ReservationRequestByIdType(1, "bike");
    List<ReservationDto> dtos = service.getReservationsByTypeAndId(req);
    assertEquals(1, dtos.size());
    assertEquals(1, dtos.get(0).vehicleId());
  }

  // Stub DAOs
  private static class StubReservationDao implements ReservationDao {
    List<Reservation> toReturn = new ArrayList<>();
    boolean addedCalled = false;
    Reservation addedRes;
    boolean deleteAllCalled = false;
    String deleteAllEmail;
    List<Reservation> byTypeId = new ArrayList<>();

    @Override
    public void add(Reservation reservation) {
      addedCalled = true;
      addedRes = reservation;
    }

    @Override
    public ArrayList<Reservation> getAll() {
      return new ArrayList<>(toReturn);
    }

    @Override
    public ArrayList<Reservation> getByTypeAndId(int vehicleId, String vehicleType) {
      return new ArrayList<>(byTypeId);
    }

    @Override
    public void deleteAll(String email) {
      deleteAllCalled = true;
      deleteAllEmail = email;
    }

    // other methods not used
    @Override public Reservation create(Reservation reservation) { return null; }
    @Override public ArrayList<Reservation> getByDate(Date date) { return null; }
    @Override public ArrayList<Reservation> getByReserveEmail(String reservedEmail) { return null; }
    @Override public void delete(Reservation reservation) {}
    @Override public void save(Reservation reservation, Reservation oldReservation) {}

    @Override public void deleteByVehicleId(int vehicleId) throws SQLException
    {

    }
  }

  private static class StubVehicleDao implements VehicleDao {
    Vehicle nextVehicle;
    boolean savedCalled = false;
    Vehicle savedNew;
    Vehicle savedOld;

    @Override
    public Vehicle getByIdAndType(int id, String vehicleType) throws SQLException {
      return nextVehicle;
    }

    @Override
    public void save(Vehicle vehicle, Vehicle oldVehicle) throws SQLException {
      savedCalled = true;
      savedNew = vehicle;
      savedOld = oldVehicle;
    }

    @Override
    public void add(Vehicle vehicle) throws SQLException {
    }

    @Override
    public ArrayList<Vehicle> getAll() throws SQLException {
      return new ArrayList<>();
    }

    @Override
    public ArrayList<Vehicle> getByType(String type) throws SQLException {
      return new ArrayList<>();
    }

    @Override
    public ArrayList<Vehicle> getByState(String state) throws SQLException {
      return new ArrayList<>();
    }

    @Override
    public ArrayList<Vehicle> getByOwnerEmail(String ownerEmail) throws SQLException {
      return new ArrayList<>();
    }

    @Override
    public void delete(Vehicle vehicle) throws SQLException {
    }

    @Override
    public void deleteAll(String request) throws SQLException {
    }

  }
}
