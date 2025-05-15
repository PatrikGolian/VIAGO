package dtos.auth;

import java.io.Serializable;

public record RegisterUserDto(String email, String password,
                              String firstName, String lastName)
    implements Serializable
{
  @Override public String toString()
  {
    return "RegisterUserRequest{" + "email='" + email + '\'' + ", password='"
        + password + '\'' + ", firstName='" + firstName + '\'' + ", lastName='"
        + lastName + '\'' + '}';
  }
}
