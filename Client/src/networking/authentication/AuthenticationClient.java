package networking.authentication;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.user.UserDataDto;
import model.exceptions.ValidationException;

public interface AuthenticationClient
{
  void registerUser(RegisterUserRequest user) throws ValidationException;
  UserDataDto login(LoginRequest loginRequest);
}
