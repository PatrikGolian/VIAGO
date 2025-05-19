package model.entities;

public class User
{

  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private boolean isAdmin;
  private boolean isBlacklisted;
  private String blacklistReason;

  public User(String email, String password, String firstName, String lastName)
  {
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.isAdmin = false;
    isBlacklisted = false;
    blacklistReason = "";
  }

  public User(String email, String password, String firstName, String lastName,
      boolean isAdmin, boolean isBlacklisted, String blacklistReason)
  {
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.isAdmin = isAdmin;
    this.isBlacklisted = isBlacklisted;
    this.blacklistReason = blacklistReason;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public boolean isAdmin()
  {
    return isAdmin;
  }

  public boolean isBlacklisted()
  {
    return isBlacklisted;
  }

  public void blacklist(String reason)
  {
    isBlacklisted = true;
    blacklistReason = reason;
  }

  public void setBlacklisted(boolean blacklisted)
  {
    isBlacklisted = blacklisted;
  }

  public void setReason(String reason)
  {
    blacklistReason = reason;
  }

  public void setAdmin(boolean promoteToAdmin)
  {
    isAdmin = promoteToAdmin;
  }

  public void setPassword(String newPassword)
  {
    password = newPassword;
  }

  public String getBlacklistReason()
  {
    return blacklistReason;
  }

  @Override public String toString()
  {
    return "User{" + ", email='" + email + '\'' + ", password='" + password
        + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
        + '\'' + ", isAdmin=" + isAdmin + ", isBlacklisted=" + isBlacklisted
        + ", blacklistReason='" + blacklistReason + '\'' + '}';
  }
}
