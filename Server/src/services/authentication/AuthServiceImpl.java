package services.authentication;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.user.UserDataDto;
import model.entities.User;
import model.exceptions.ValidationException;
import persistence.user.UserDao;

import java.sql.SQLException;

public class AuthServiceImpl implements AuthenticationService
{

  private final UserDao userDao;

  public AuthServiceImpl(UserDao userDao)
  {
    this.userDao = userDao;
  }

  @Override public void registerUser(RegisterUserRequest request)
      throws SQLException
  {
    User existingUser = userDao.getSingle(request.email());
    if (existingUser != null)
    {
      throw new ValidationException("Email is already in use.");
    }

    validateEmailIsCorrectFormat(request.email());
    validatePasswordIsCorrectFormat(request.password());
    validateFirstName(request.firstName());
    validateLastName(request.lastName());
    if (request.email().matches(("^[a-zA-Z]+@via\\.dk$")))
    {
      User newUser = new User(

          request.email(), request.password(), request.firstName(),
          request.lastName());
      newUser.setAdmin(true);
      userDao.add(newUser);
    }
    else
    {
      User newUser = new User(request.email(), request.password(),
          request.firstName(), request.lastName());
      userDao.add(newUser);
    }
  }

  private static void validatePasswordIsCorrectFormat(String password)
  {
    // validate password has correct format, e.g. upper case/lower case, symbols, numbers, etc
    if (password.length() < 8)
    {
      throw new ValidationException("Password must be 8 or more characters");
    }
    if (password.length() > 24)
    {
      throw new ValidationException("Password must be 24 or fewer characters");
    }
    // etc..
  }

  private static void validateFirstName(String name)
  {
    if (name.isEmpty() || name == null)
    {
      throw new ValidationException("First name cannot be empty");
    }
    else if (name.length() < 2)
    {
      throw new ValidationException(
          "First name has to have at least 3 letters");
    }
    else if (!name.matches("[a-zA-Z ]+"))
    {
      throw new ValidationException("First name can only contain letters");
    }
  }

  private static void validateLastName(String name)
  {
    if (name.isEmpty() || name == null)
    {
      throw new ValidationException("Last name cannot be empty");
    }
    else if (name.length() < 2)
    {
      throw new ValidationException(
          "Last name has to have at least 3 letters.");
    }
    else if (!name.matches("[a-zA-Z ]+"))
    {
      throw new ValidationException("Last name can only contain letters");
    }
  }

  private static void validateEmailIsCorrectFormat(String email)
  {
    // validate email has correct format
    if (!email.matches("^\\d{6,}@via\\.dk$") && !email.matches(
        "^[a-zA-Z]+@via\\.dk$"))
    {
      throw new ValidationException(
          "Email needs to be a valid VIA UC email (format: user@via.dk)");
    }
  }

  @Override public UserDataDto login(LoginRequest request) throws SQLException
  {
    User existingUser = userDao.getSingle(request.email());
    if (existingUser == null)
    {
      throw new ValidationException("User not found.");
    }

    if (!existingUser.getPassword().equals(request.password()))
    {
      throw new ValidationException("Incorrect password.");
    }

    if (existingUser.isBlacklisted())
    {
      throw new ValidationException(
          "This user is blacklisted: " + existingUser.getBlacklistReason());
    }

    UserDataDto userData = new UserDataDto(existingUser.getEmail(),
        existingUser.getFirstName(), existingUser.getLastName(),
        existingUser.isBlacklisted(), existingUser.isAdmin());
    return userData;
  }
  }
