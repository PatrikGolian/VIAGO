package persistence.user;

import model.entities.User;
import model.exceptions.ServerFailureException;
import parser.ParserException;
import parser.XmlJsonParser;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserJsonFileDao implements UserDao
{

  private final XmlJsonParser parser = new XmlJsonParser();

  public UserJsonFileDao()
  {
    if (!new File("Users.json").exists())
    {
      UserList users = new UserList(new ArrayList<>(Arrays.asList()));
      try
      {
        parser.toJson(users, "Users.json");
      }
      catch (ParserException e)
      {
        throw new ServerFailureException(e.getMessage());
      }
    }
  }

  @Override public void add(User user)
  {
    try
    {
      UserList users = parser.fromJson("Users.json", UserList.class);
      users.list.add(user);
      parser.toJson(users, "Users.json");
    }
    catch (ParserException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public User getSingle(String email)
  {
    try
    {
      UserList users = parser.fromJson("Users.json", UserList.class);
      for (User user : users.list)
      {
        if (user.getEmail().equals(email))
        {
          return user;
        }
      }
    }
    catch (ParserException e)
    {
      throw new RuntimeException(e);
    }

    return null;
  }

  @Override public void delete(String email)
  {

  }

  @Override public void save(User user)
  {
    try
    {
      // could delete existing with above delete()
      // then use add()

      UserList users = parser.fromJson("Users.json", UserList.class);

      User userToDelete = null;
      for (User potentialUser : users.list)
      {
        if (potentialUser.getEmail().equals(user.getEmail()))
        {
          userToDelete = potentialUser;
        }
      }

      users.list.remove(userToDelete);

      users.list.add(user);

      parser.toJson(users, "Users.json");
    }
    catch (ParserException e)
    {
      throw new RuntimeException(e);
    }

  }

  @Override public List<User> getMany(String firstNameContains)
  {
    try
    {
      UserList users = parser.fromJson("Users.json", UserList.class);

      List<User> filtered = new ArrayList<>();

      for (User user : users.list)
      {
        if (firstNameContains == null || user.getFirstName().toLowerCase()
            .contains(firstNameContains.toLowerCase()))
        {
          filtered.add(user);
        }
      }

      return filtered;
    }
    catch (ParserException e)
    {
      throw new RuntimeException("Failed to load users", e);
    }
  }

  @Override public void updateName(String email, String fname, String lname) throws SQLException
  {

  }

  @Override public void updatePassword(String email, String password) throws SQLException
  {

  }
}
