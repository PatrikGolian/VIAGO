package persistence.user;

import model.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserPostgresDao implements UserDao
{
  private static UserPostgresDao instance;

  private UserPostgresDao() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  private static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=viago",
        "postgres", "password");
  }

  public static synchronized UserPostgresDao getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new UserPostgresDao();
    }
    return instance;
  }

  public User create(String email, String password, String firstname,
      String lastname, boolean isAdmin, boolean isBlackListed,
      String blackListReason) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO account(email, password, firstname, lastname, isAdmin, isBlackListed, blackListReason) VALUES(?,?,?,?,?,?,?)");
      statement.setString(1, email);
      statement.setString(2, password);
      statement.setString(3, firstname);
      statement.setString(4, lastname);
      statement.setBoolean(5, isAdmin);
      statement.setBoolean(6, isBlackListed);
      statement.setString(7, blackListReason);
      statement.executeUpdate();

      return new User(email, password, firstname, lastname, isAdmin,
          isBlackListed, blackListReason);
    }

  }

  public void changeUser(String email, String password, String firstname,
      String lastname, boolean isAdmin, boolean isBlackListed,
      String blackListReason) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE account SET password = ?, firstname = ?, lastname = ?, isAdmin = ?, isBlackListed = ?, blackListReason = ? WHERE email = ?");
      statement.setString(7, email);
      statement.setString(1, password);
      statement.setString(2, firstname);
      statement.setString(3, lastname);
      statement.setBoolean(4, isAdmin);
      statement.setBoolean(5, isBlackListed);
      statement.setString(6, blackListReason);
      statement.executeUpdate();
    }

  }

  @Override public void change(User user) throws SQLException
  {
    User temp = getSingle(user.getEmail());

    changeUser(
        user.getEmail(), user.getPassword(), user.getFirstName(),
        user.getLastName(), temp.isAdmin(), temp.isBlacklisted(),
        temp.getBlacklistReason()
    );

  }

  @Override public void add(User user) throws SQLException
  {
    create(user.getEmail(), user.getPassword(), user.getFirstName(),
        user.getLastName(), user.isAdmin(), user.isBlacklisted(),
        user.getBlacklistReason());
  }

  @Override public User getSingle(String email) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT* FROM account WHERE email =?");
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        String password = resultSet.getString("password");
        String firstName = resultSet.getString("firstname");
        String lastName = resultSet.getString("lastname");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        boolean isBlackListed = resultSet.getBoolean("isBlackListed");
        String blacklistReason = resultSet.getString("blackListReason");
        return new User(email, password, firstName, lastName, isAdmin,
            isBlackListed, blacklistReason);

      }
      else
      {
        return null;
      }
    }
  }

  @Override public void delete(String email) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM account WHERE email = ?");
      statement.setString(1, email);
      statement.executeUpdate();
    }
  }

  @Override public void save(User user) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE account SET firstname = ?, lastname = ?, isBlackListed = ?, blackListReason = ? WHERE email = ?");
      statement.setString(1, user.getFirstName());
      statement.setString(2, user.getLastName());
      statement.setBoolean(3, user.isBlacklisted());
      statement.setString(4, user.getBlacklistReason());
      statement.setString(5, user.getEmail());
      statement.executeUpdate();
    }
  }

  @Override public List<User> getMany(String firstNameContains)
      throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT* FROM account WHERE firstname LIKE ?");
      statement.setString(1, "%" + firstNameContains + "%");
      ResultSet resultSet = statement.executeQuery();
      ArrayList<User> result = new ArrayList<>();
      while (resultSet.next())
      {
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String firstName = resultSet.getString("firstname");
        String lastName = resultSet.getString("lastname");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        boolean isBlackListed = resultSet.getBoolean("isBlackListed");
        String blacklistReason = resultSet.getString("blackListReason");
        result.add(new User(email, password, firstName, lastName, isAdmin,
            isBlackListed, blacklistReason));
      }
      return result;
    }
  }
}