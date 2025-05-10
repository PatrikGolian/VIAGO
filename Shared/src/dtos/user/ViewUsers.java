package dtos.user;

import java.io.Serializable;

// I have a wrapper abstract class, because the request and dto belong together.
// This is a way to have them together in the same file.
// If you plan on re-using the UserDto other places, maybe it should be moved to its own file.
public abstract class ViewUsers
{
  public record Request(String firstNameContains) implements Serializable
  {
    @Override public String toString()
    {
      return "Request{" + ", firstNameContains='" + firstNameContains + '\''
          + '}';
    }
  }

  public record UserDisplayDto(String email, String firstName, String lastName,
                               boolean isBlacklisted) implements Serializable
  {
  }
}

