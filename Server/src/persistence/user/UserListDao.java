package persistence.user;

import model.entities.User;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserListDao implements UserDao
{

  private final static List<User> users = new ArrayList<>(Arrays.asList(

  ));

  public UserListDao()
  {
    users.get(0).setAdmin(true);
  }

  @Override public void add(User user)
  {
    users.add(user);
  }

  /**
   * Find a User entity by a given email.
   * Return null, if no matching user was found.
   * I could have thrown an exception instead, it might be simpler.
   *
   * @param email
   * @return
   */
  @Override public User getSingle(String email)
  {
    for (User user : users)
    {
      if (email.equals(user.getEmail()))
      {
        return user;
      }
    }
    return null;
  }

  @Override public void delete(String email)
  {
    users.remove(getSingle(email));
    // Will implement later. Just remove from list.
  }

  @Override public void save(User user)
  {
    // simulate updating user, by first removing existing, then inserting updated user
    users.removeIf(usr -> usr.getEmail().equals(user.getEmail()));
    System.out.println("updated: " + user);
    users.add(user);
  }

  @Override public List<User> getMany(String firstNameContains)
  {
    List<User> result = new ArrayList<>();
    for (int i = 0; i < users.size(); i++)
    {
      // this is an attempt at implementing paging, as seen in dbs, maybe it works, maybe it doesn't
      User user = users.get(i);

      if (StringUtils.isNullOrEmpty(firstNameContains) || user.getFirstName()
          .contains(
              firstNameContains)) // if argument firstNameContains is not empty string, we filter by this.
      {
        result.add(user);
      }
    }
    return result;
  }
}
