package dtos.user;

import java.io.Serializable;

public record UserDataDto(String email, String firstName, String lastName,
                          boolean isBlacklisted, boolean isAdmin, String blackListReason)
    implements Serializable
{
  @Override public String toString()
  {
    return "UserDataDto{" + "email='" + email + '\'' + ", firstName='"
        + firstName + '\'' + ", lastName='" + lastName + '\''
        + ", isBlacklisted=" + isBlacklisted + ", isAdmin=" + isAdmin + ", blackListReason=" + blackListReason+ '}';
  }
}
