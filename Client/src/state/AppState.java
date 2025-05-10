package state;

import dtos.user.UserDataDto;

public class AppState
{
  private static UserDataDto loggedInUser;

  public static UserDataDto getCurrentUser()
  {
    return loggedInUser;
  }

  public static void setCurrentUser(UserDataDto user)
  {
    loggedInUser = user;
  }
}
