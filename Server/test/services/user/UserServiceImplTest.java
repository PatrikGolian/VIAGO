package services.user;

import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.BlacklistUserRequest;
import dtos.user.UpdatePasswordRequest;
import dtos.user.UserDataDto;
import model.entities.User;
import model.exceptions.NotFoundException;
import model.exceptions.ValidationException;
import persistence.user.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest{
  private static final String VALID_LETTER_EMAIL = "abcdef@via.dk";
  private static final String VALID_DIGIT_EMAIL  = "123456@via.dk";
  private static final String INVALID_EMAIL      = "ab12cd@via.dk"; // mixed letters/digits
  private static final String INVALID_DIGIT_SHORT = "12345@via.dk";   // 5 digits
  private static final String INVALID_DIGIT_LONG  = "1234567@via.dk"; // 7 digits

  private UserServiceImpl service;
  private StubUserDao stubDao;

  @BeforeEach
  void setUp() {
    stubDao = new StubUserDao();
    service = new UserServiceImpl(stubDao);
  }

  @Test
  void changeUser_nullRequest_throwsNPE() {
    assertThrows(NullPointerException.class, () -> service.changeUser(null));
  }

  @Test
  void changeUser_userNotFound_throwsValidationException() throws SQLException {
    stubDao.singleUser = null;
    ChangeUserRequest req = new ChangeUserRequest("First", "Last", VALID_LETTER_EMAIL, null);
    ValidationException ex = assertThrows(ValidationException.class, () -> service.changeUser(req));
    assertEquals("User not found", ex.getMessage());
    assertFalse(stubDao.updateNameCalled);
  }

  @Test
  void changeUser_blankPassword_onlyUpdatesName() throws SQLException {
    stubDao.singleUser = new User(VALID_LETTER_EMAIL, "pwd", "F", "L", false, false, "");
    ChangeUserRequest req = new ChangeUserRequest("NewF", "NewL", VALID_LETTER_EMAIL, "   ");
    service.changeUser(req);
    assertTrue(stubDao.updateNameCalled);
    assertEquals(VALID_LETTER_EMAIL, stubDao.lastUpdateNameEmail);
    assertEquals("NewF", stubDao.lastFirstName);
    assertEquals("NewL", stubDao.lastLastName);
    assertFalse(stubDao.updatePasswordCalled);
  }

  @Test
  void changeUser_newPasswordDifferent_updatesPassword() throws SQLException {
    stubDao.singleUser = new User(VALID_LETTER_EMAIL, "oldpwd", "F", "L", false, false, "");
    ChangeUserRequest req = new ChangeUserRequest("F2", "L2", VALID_LETTER_EMAIL, "newpwd");
    service.changeUser(req);
    assertTrue(stubDao.updateNameCalled);
    assertTrue(stubDao.updatePasswordCalled);
    assertEquals(VALID_LETTER_EMAIL, stubDao.lastUpdatePasswordEmail);
    assertEquals("newpwd", stubDao.lastNewPassword);
  }

  @Test
  void changeUser_invalidMixedEmail_throwsValidationException() {
    ChangeUserRequest req = new ChangeUserRequest("F", "L", INVALID_EMAIL, null);
    assertThrows(ValidationException.class, () -> service.changeUser(req));
  }

  @Test
  void changeUser_invalidShortDigitEmail_throwsValidationException() {
    ChangeUserRequest req = new ChangeUserRequest("F", "L", INVALID_DIGIT_SHORT, null);
    assertThrows(ValidationException.class, () -> service.changeUser(req));
  }

  @Test
  void changeUser_invalidLongDigitEmail_throwsValidationException() {
    ChangeUserRequest req = new ChangeUserRequest("F", "L", INVALID_DIGIT_LONG, null);
    assertThrows(ValidationException.class, () -> service.changeUser(req));
  }

  @Test
  void getPassword_returnsPassword() throws SQLException {
    stubDao.singleUser = new User(VALID_DIGIT_EMAIL, "secret", "A", "B", false, false, "");
    GetPasswordRequest req = new GetPasswordRequest(VALID_DIGIT_EMAIL);
    assertEquals("secret", service.getPassword(req));
  }

  @Test
  void getPassword_daoError_throwsSQLException() throws SQLException {
    stubDao.failGetSingle = true;
    GetPasswordRequest req = new GetPasswordRequest(VALID_LETTER_EMAIL);
    assertThrows(SQLException.class, () -> service.getPassword(req));
  }

  @Test
  void getUsersOverview_zeroUsers() throws SQLException {
    stubDao.manyUsers = Collections.emptyList();
    List<UserDataDto> dtos = service.getUsersOverview();
    assertNotNull(dtos);
    assertTrue(dtos.isEmpty());
  }

  @Test
  void getUsersOverview_manyUsers() throws SQLException {
    User u1 = new User(VALID_LETTER_EMAIL, "p1", "A", "B", false, true, "reason1");
    User u2 = new User(VALID_DIGIT_EMAIL, "p2", "C", "D", true, false, "");
    stubDao.manyUsers = Arrays.asList(u1, u2);
    List<UserDataDto> dtos = service.getUsersOverview();
    assertEquals(2, dtos.size());
    assertEquals(VALID_LETTER_EMAIL, dtos.get(0).email());
    assertTrue(dtos.get(0).isBlacklisted());
    assertFalse(dtos.get(1).isBlacklisted());
  }

  @Test
  void blacklistUser_userNotFound_throwsNotFoundException() throws SQLException {
    stubDao.singleUser = null;
    BlacklistUserRequest req = new BlacklistUserRequest(VALID_LETTER_EMAIL, "r");
    assertThrows(NotFoundException.class, () -> service.blacklistUser(req));
  }

  @Test
  void blacklistUser_firstTime_blacklistsUser() throws SQLException {
    stubDao.singleUser = new User(VALID_LETTER_EMAIL, "p", "A", "B", false, false, "");
    BlacklistUserRequest req = new BlacklistUserRequest(VALID_LETTER_EMAIL, "reason");
    service.blacklistUser(req);
    assertTrue(stubDao.savedUser.isBlacklisted());
    assertEquals("reason", stubDao.savedUser.getBlacklistReason());
  }

  @Test
  void blacklistUser_alreadyBlacklisted_unblacklistsUser() throws SQLException {
    stubDao.singleUser = new User(VALID_LETTER_EMAIL, "p", "A", "B", false, true, "old");
    BlacklistUserRequest req = new BlacklistUserRequest(VALID_LETTER_EMAIL, "new");
    service.blacklistUser(req);
    assertFalse(stubDao.savedUser.isBlacklisted());
  }

  @Test
  void blacklistUserReason_savesReasonOnly() throws SQLException {
    stubDao.singleUser = new User(VALID_LETTER_EMAIL, "p", "A", "B", false, false, "");
    BlacklistUserRequest req = new BlacklistUserRequest(VALID_LETTER_EMAIL, "r");
    service.blacklistUserReason(req);
    assertEquals("r", stubDao.savedUser.getBlacklistReason());
  }

  @Test
  void getBlackListReason_returnsReason() throws SQLException {
    stubDao.singleUser = new User(VALID_DIGIT_EMAIL, "p", "A", "B", false, true, "reason");
    BlacklistUserRequest req = new BlacklistUserRequest(VALID_DIGIT_EMAIL, "ignored");
    assertEquals("reason", service.getBlackListReason(req));
  }

  @Test
  void updatePassword_userNotFound_throwsNotFoundException() throws SQLException {
    stubDao.singleUser = null;
    UpdatePasswordRequest req = new UpdatePasswordRequest(VALID_LETTER_EMAIL, "old", "new");
    assertThrows(NotFoundException.class, () -> service.updatePassword(req));
  }

  @Test
  void updatePassword_incorrectOldPassword_throwsValidationException() throws SQLException {
    stubDao.singleUser = new User(VALID_LETTER_EMAIL, "oldpwd", "A", "B", false, false, "");
    UpdatePasswordRequest req = new UpdatePasswordRequest(VALID_LETTER_EMAIL, "wrong", "new");
    ValidationException ex = assertThrows(ValidationException.class, () -> service.updatePassword(req));
    assertEquals("Incorrect password", ex.getMessage());
  }

  @Test
  void updatePassword_success_updatesAndSaves() throws SQLException {
    stubDao.singleUser = new User(VALID_DIGIT_EMAIL, "oldpwd", "A", "B", false, false, "");
    UpdatePasswordRequest req = new UpdatePasswordRequest(VALID_DIGIT_EMAIL, "oldpwd", "newpwd");
    service.updatePassword(req);
    assertNotNull(stubDao.savedUser);
    assertEquals("newpwd", stubDao.savedUser.getPassword());
  }

  private static class StubUserDao implements UserDao {
    User singleUser;
    List<User> manyUsers;
    boolean failGetSingle = false;
    boolean updateNameCalled = false;
    String lastUpdateNameEmail;
    String lastFirstName;
    String lastLastName;
    boolean updatePasswordCalled = false;
    String lastUpdatePasswordEmail;
    String lastNewPassword;
    User savedUser;

    @Override public void add(User user) throws SQLException { /* no-op */ }
    @Override public void updateName(String email, String firstName, String lastName) throws SQLException {
      updateNameCalled = true; lastUpdateNameEmail = email; lastFirstName = firstName; lastLastName = lastName;
    }
    @Override public void updatePassword(String email, String password) throws SQLException {
      updatePasswordCalled = true; lastUpdatePasswordEmail = email; lastNewPassword = password;
    }
    @Override public User getSingle(String email) throws SQLException {
      if (failGetSingle) throw new SQLException("getSingle failure");
      return singleUser;
    }
    @Override public void delete(String email) throws SQLException { /* no-op */ }
    @Override public void save(User user) throws SQLException { savedUser = user; }
    @Override public List<User> getMany() throws SQLException { return manyUsers; }
  }
}
