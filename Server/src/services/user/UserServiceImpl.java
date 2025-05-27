package services.user;

import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.*;
import model.entities.User;
import model.exceptions.NotFoundException;
import model.exceptions.ValidationException;
import persistence.user.UserDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService
{
  private final UserDao userDao;

  public UserServiceImpl(UserDao userDao)
  {
    this.userDao = userDao;
  }

  @Override public void changeUser(ChangeUserRequest request)
      throws SQLException
  {
    User user = userDao.getSingle(request.email());

    if(user == null)
    {
      throw new ValidationException("User not found");
    }

    userDao.updateName(request.email(),request.firstName(), request.lastName());

    String newPassword = request.password();

    if(newPassword != null && !newPassword.isBlank() && !newPassword.equals(user.getPassword()))
    {
      userDao.updatePassword(request.email(), newPassword);
    }
  }

  @Override public String getPassword(GetPasswordRequest request)
      throws SQLException
  {
    User user = userDao.getSingle(request.email());
    return user.getPassword();
  }

  @Override public void blacklistUser(BlacklistUserRequest request)
      throws SQLException
  {
    User user = userDao.getSingle(request.email());
    if (user == null)
    {
      throw new NotFoundException(
          "User with email '" + request.email() + "' not found.");
    }
    user.setReason(request.reason());
    System.out.println(user.toString());
    if (user.isBlacklisted())
    {
      user.setBlacklisted(!(user.isBlacklisted()));
    }
    else
    {
      user.blacklist(request.reason()); // Alternatively I could have a setBlackList(true, request.reason()) method. I prefer this clearer approach.
    }
    userDao.save(user);
  }
  @Override public void blacklistUserReason(BlacklistUserRequest request)
      throws SQLException
  {
    User user = userDao.getSingle(request.email());
    if (user == null)
    {
      throw new NotFoundException(
          "User with email '" + request.email() + "' not found.");
    }
    user.setReason(request.reason());
    System.out.println(user.toString());
    userDao.save(user);
  }

  @Override public  String getBlackListReason(BlacklistUserRequest request) throws SQLException
  {
    User user = userDao.getSingle(request.email());
    if (user == null)
    {
      throw new NotFoundException(
          "User with email '" + request.email() + "' not found.");
    }
    return user.getBlacklistReason();
  }

  @Override public List<UserDataDto> getUsersOverview(
      ) throws SQLException
  {
    List<User> users = userDao.getMany();
    List<UserDataDto> result = new ArrayList<>();

    // convert User to UserDto. This way we only send the data, the client needs. We don't include the password, for example.
    // I could add admin status or black list status, if needed.

    for (User user : users)
    {
      UserDataDto dto = new UserDataDto(
          user.getEmail(), user.getFirstName(), user.getLastName(),
          user.isBlacklisted(),user.isAdmin(),user.getBlacklistReason());
      result.add(dto);
    }

    return result;
  }

  @Override public void updatePassword(UpdatePasswordRequest request)
      throws SQLException
  {
    // retrieve
    User user = userDao.getSingle(request.email());
    if (user == null)
    {
      throw new NotFoundException(
          "User with email '" + request.email() + "' not found.");
    }

    // validate
    if (!user.getPassword().equals(request.oldPassword()))
    {
      throw new ValidationException("Incorrect password");
    }

    // update
    user.setPassword(request.newPassword());

    // save changes
    userDao.save(user);
  }
}
