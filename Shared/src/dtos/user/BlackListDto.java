package dtos.user;

import java.io.Serializable;

public record BlackListDto(String email, String blackListReason)
    implements Serializable
{

  @Override public String toString()
  {
    return "UserDataDto{" + "email='" + email + '\'' + ", blackListReason=" + blackListReason+ '}';
  }
}


