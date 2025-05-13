package dtos.studentAuth;

import java.io.Serializable;

public record GetPasswordRequest(String email)
    implements Serializable
{
  @Override public String toString()
  {
    return "RegisterUserRequest{" + "email='" + email + '}';
  }
}