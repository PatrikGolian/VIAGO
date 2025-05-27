package services.authentication;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.user.UserDataDto;
import model.entities.User;
import model.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.user.UserDao;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceImplTest {
  private AuthServiceImpl service;
  private StubUserDao stubDao;

  @BeforeEach
  void setUp() {
    stubDao = new StubUserDao();
    service = new AuthServiceImpl(stubDao);
  }

  @Test
  void registerUser_nullRequest_throwsNPE() {
    assertThrows(NullPointerException.class, () -> service.registerUser(null));
  }

  @Test
  void registerUser_existingEmail_throwsValidationException() {
    stubDao.existingUser = new User("111111@via.dk", "password", "First", "Last");
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "password", "First", "Last");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("Email is already in use.", ex.getMessage());
    assertNull(stubDao.addedUser);
  }


  @Test
  void registerUser_invalidEmailFormat_throwsValidationException() {
    RegisterUserRequest req = new RegisterUserRequest("user@other.com", "password", "First", "Last");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("Email needs to be a valid VIA UC email (format: user@via.dk)", ex.getMessage());
  }


  @Test
  void registerUser_shortPassword_throwsValidationException() {
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "short", "First", "Last");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("Password must be 8 or more characters", ex.getMessage());
  }


  @Test
  void registerUser_longPassword_throwsValidationException() {
    String longPwd = "a".repeat(25);
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", longPwd, "First", "Last");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("Password must be 24 or fewer characters", ex.getMessage());
  }

  @Test
  void registerUser_emptyFirstName_throwsValidationException() {
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "password", "", "Last");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("First name cannot be empty", ex.getMessage());
  }

  @Test
  void registerUser_shortFirstName_throwsValidationException() {
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "password", "A", "Last");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("First name has to have at least 3 letters", ex.getMessage());
  }

  @Test
  void registerUser_invalidFirstNameChars_throwsValidationException() {
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "password", "Jo3", "Last");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("First name can only contain letters", ex.getMessage());
  }

  @Test
  void registerUser_emptyLastName_throwsValidationException() {
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "password", "First", "");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("Last name cannot be empty", ex.getMessage());
  }

  @Test
  void registerUser_shortLastName_throwsValidationException() {
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "password", "First", "B");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("Last name has to have at least 3 letters.", ex.getMessage());
  }

  @Test
  void registerUser_invalidLastNameChars_throwsValidationException() {
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "password", "First", "La5");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.registerUser(req));
    assertEquals("Last name can only contain letters", ex.getMessage());
  }

  @Test
  void registerUser_letterEmail_setsAdminTrue() throws SQLException {
    RegisterUserRequest req = new RegisterUserRequest("abcdef@via.dk", "password123", "First", "Last");
    service.registerUser(req);
    assertNotNull(stubDao.addedUser);
    assertTrue(stubDao.addedUser.isAdmin());
  }

  @Test
  void registerUser_digitEmail_setsAdminFalse() throws SQLException {
    RegisterUserRequest req = new RegisterUserRequest("123456@via.dk", "password123", "First", "Last");
    service.registerUser(req);
    assertNotNull(stubDao.addedUser);
    assertFalse(stubDao.addedUser.isAdmin());
  }

  @Test
  void registerUser_daoGetThrowsSQLException() throws SQLException {
    stubDao.failGet = true;
    RegisterUserRequest req = new RegisterUserRequest("111111@via.dk", "password123", "First", "Last");
    assertThrows(SQLException.class, () -> service.registerUser(req));
  }

  @Test
  void login_nullRequest_throwsNPE() {
    assertThrows(NullPointerException.class, () -> service.login(null));
  }

  @Test
  void login_unknownUser_throwsValidationException() throws SQLException {
    stubDao.existingUser = null;
    LoginRequest req = new LoginRequest("user@via.dk", "pwd");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.login(req));
    assertEquals("User not found.", ex.getMessage());
  }

  @Test
  void login_wrongPassword_throwsValidationException() throws SQLException {
    stubDao.existingUser = new User("111111@via.dk", "correctPwd", "First", "Last");
    LoginRequest req = new LoginRequest("111111@via.dk", "wrongPwd");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.login(req));
    assertEquals("Incorrect password.", ex.getMessage());
  }

  @Test
  void login_blacklistedUser_throwsValidationException() throws SQLException {
    User u = new User("111111@via.dk", "pwd", "First", "Last");
    u.setBlacklisted(true);
    u.setReason("nope");
    stubDao.existingUser = u;
    LoginRequest req = new LoginRequest("111111@via.dk", "pwd");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.login(req));
    assertTrue(ex.getMessage().contains("This user is blacklisted: nope"));
  }

  @Test
  void login_success_returnsUserDataDto() throws SQLException {
    User u = new User("111111@via.dk", "pwd12345", "First", "Last");
    stubDao.existingUser = u;
    LoginRequest req = new LoginRequest("111111@via.dk", "pwd12345");
    UserDataDto dto = service.login(req);
    assertEquals(u.getEmail(), dto.email());
    assertEquals(u.getFirstName(), dto.firstName());
    assertEquals(u.getLastName(), dto.lastName());
    assertEquals(u.isBlacklisted(), dto.isBlacklisted());
    assertEquals(u.isAdmin(), dto.isAdmin());
    assertEquals(u.getBlacklistReason(), dto.blackListReason());
  }

  private static class StubUserDao implements UserDao {
    User existingUser;
    User addedUser;
    boolean failGet = false;

    @Override
    public User getSingle(String email) throws SQLException {
      if (failGet) throw new SQLException("get error");
      return existingUser;
    }

    @Override
    public void add(User user) throws SQLException {
      addedUser = user;
    }

    @Override public void updateName(String email, String fname, String lname) throws SQLException {}
    @Override public void updatePassword(String email, String password) throws SQLException {}
    @Override public void delete(String email) throws SQLException {}
    @Override public void save(User user) throws SQLException {}
    @Override public java.util.List<User> getMany() throws SQLException { return null; }
  }
}
