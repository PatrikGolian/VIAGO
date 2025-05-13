package dtos.studentAuth;

import java.io.Serializable;

public record ChangeUserRequest(String firstName, String lastName,
                                  String email, String password)
    implements Serializable
{
  @Override public String toString()
  {
    return "RegisterUserRequest{" + "email='" + email + '\'' + ", password='"
        + password + '\'' + ", firstName='" + firstName + '\'' + ", lastName='"
        + lastName + '\'' + '}';
  }
}