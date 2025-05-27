package services.studentaccount;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import model.entities.User;
import model.entities.reservation.Reservation;
import model.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.reservation.ReservationDao;
import persistence.user.UserDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentAccountServiceImplTest {
  private StudentAccountServiceImpl service;
  private StubReservationDao stubRes;
  private StubUserDao stubUser;

  @BeforeEach
  void setUp() {
    stubRes = new StubReservationDao();
    stubUser = new StubUserDao();
    service = new StudentAccountServiceImpl(stubRes, stubUser);
  }

  @Test
  void getReservationsOverview_zero_returnsEmpty() throws SQLException {
    stubRes.toReturn = new ArrayList<>();
    List<ReservationDto> dtos = service.getReservationsOverview(
        new ReservationReserveRequest("222222@via.dk"));
    assertNotNull(dtos);
    assertTrue(dtos.isEmpty());
  }

  @Test
  void getReservationsOverview_many_returnsDtos() throws SQLException {
    Reservation r1 = new Reservation(1, "bike", "111111@via.dk", "222222@via.dk",
        stubRes.date(1), stubRes.date(2), 50);
    Reservation r2 = new Reservation(2, "scooter", "111111@via.dk", "222222@via.dk",
        stubRes.date(3), stubRes.date(4), 75);
    stubRes.toReturn = new ArrayList<>(List.of(r1, r2));
    List<ReservationDto> dtos = service.getReservationsOverview(
        new ReservationReserveRequest("222222@via.dk"));
    assertEquals(2, dtos.size());
    assertEquals(1, dtos.get(0).vehicleId());
    assertEquals("scooter", dtos.get(1).vehicleType());
  }

  @Test
  void getReservationsOverview_daoThrowsSQLException_propagates() throws SQLException {
    stubRes.failGet = true;
    assertThrows(SQLException.class,
        () -> service.getReservationsOverview(
            new ReservationReserveRequest("222222@via.dk")));
  }

  @Test
  void changeUser_userNotFound_throwsValidationException() throws SQLException {
    stubUser.singleUser = null;
    ChangeUserRequest req = new ChangeUserRequest("F","L","222222@via.dk","pwd");
    ValidationException ex = assertThrows(ValidationException.class,
        () -> service.changeUser(req));
    assertEquals("User not found", ex.getMessage());
  }

  @Test
  void changeUser_blankPassword_updatesNameOnly() throws SQLException {
    User u = new User("user@x","oldpwd","F","L");
    stubUser.singleUser = u;
    ChangeUserRequest req = new ChangeUserRequest("NewF","NewL","222222@via.dk","   ");
    service.changeUser(req);
    assertTrue(stubUser.updateNameCalled);
    assertFalse(stubUser.updatePasswordCalled);
  }

  @Test
  void changeUser_newPassword_updatesPassword() throws SQLException {
    User u = new User("user@x","oldpwd","F","L");
    stubUser.singleUser = u;
    ChangeUserRequest req = new ChangeUserRequest("F","L","222222@via.dk","newpwd");
    service.changeUser(req);
    assertTrue(stubUser.updateNameCalled);
    assertTrue(stubUser.updatePasswordCalled);
  }

  @Test
  void getPassword_returnsPassword() throws SQLException {
    stubUser.singleUser = new User("user@x","secret","F","L");
    String pwd = service.getPassword(new GetPasswordRequest("222222@via.dk"));
    assertEquals("secret", pwd);
  }

  @Test
  void delete_futureReservation_deletes() {
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    ReservationRequest req = new ReservationRequest(
        1, "bike", "111111@via.dk", "222222@via.dk",
        stubRes.toDate(tomorrow), stubRes.toDate(tomorrow.plusDays(1)), 20);
    service.delete(req);
    assertTrue(stubRes.deletedCalled);
  }

  @Test
  void delete_pastReservation_throwsIllegalArgumentException() {
    LocalDate today = LocalDate.now();
    ReservationRequest req = new ReservationRequest(
        1, "bike", "111111@via.dk", "222222@via.dk",
        stubRes.toDate(today), stubRes.toDate(today), 20);
    assertThrows(IllegalArgumentException.class,
        () -> service.delete(req));
  }

  private static class StubReservationDao implements ReservationDao {
    ArrayList<Reservation> toReturn = new ArrayList<>();
    boolean failGet = false;
    boolean deletedCalled = false;

    @Override
    public ArrayList<Reservation> getByReserveEmail(String email) throws SQLException {
      if (failGet) throw new SQLException("fail");
      return new ArrayList<>(toReturn);
    }

    @Override
    public void delete(Reservation reservation) throws SQLException {
      deletedCalled = true;
    }

    model.Date date(int dayOffset) { return toDate(LocalDate.now().plusDays(dayOffset)); }
    model.Date toDate(LocalDate ld) { return new model.Date(ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear()); }

    @Override public Reservation create(Reservation r) throws SQLException { return null; }
    @Override public ArrayList<Reservation> getAll() throws SQLException { return null; }
    @Override public ArrayList<Reservation> getByDate(model.Date date) throws SQLException { return null; }
    @Override public void add(Reservation r) throws SQLException {}
    @Override public void deleteAll(String email) {}
    @Override public void save(Reservation r, Reservation old) throws SQLException {}
    @Override public ArrayList<Reservation> getByTypeAndId(int id, String type) throws SQLException { return null; }
    @Override public void deleteByVehicleId(int vehicleId) throws SQLException { }
  }

  private static class StubUserDao implements UserDao {
    User singleUser;
    boolean updateNameCalled = false;
    boolean updatePasswordCalled = false;

    @Override
    public User getSingle(String email) throws SQLException {
      return singleUser;
    }

    @Override public void updateName(String email, String fname, String lname) throws SQLException { updateNameCalled = true; }
    @Override public void updatePassword(String email, String password) throws SQLException { updatePasswordCalled = true; }
    @Override public void add(User user) throws SQLException {}
    @Override public void delete(String email) throws SQLException {}
    @Override public void save(User user) throws SQLException {}
    @Override public List<User> getMany() throws SQLException { return null; }
  }
}
