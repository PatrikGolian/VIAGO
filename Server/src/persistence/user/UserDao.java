package persistence.user;

import model.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao
{
  void add(User user) throws SQLException;
  User getSingle(String email) throws SQLException;
  void delete(String email) throws SQLException;
  void save(User user) throws SQLException;
  List<User> getMany(String firstNameContains) throws SQLException;
}
