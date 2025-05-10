package persistence.user;

import model.entities.User;

import java.util.List;

public class UserList
{
  public List<User> list;

  public UserList(List<User> users)
  {
    this.list = users;
  }
}

