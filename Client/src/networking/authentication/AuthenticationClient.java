package networking.authentication;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.user.UserDataDto;

public interface AuthenticationClient
{
  void registerUser(RegisterUserRequest user);
  UserDataDto login(LoginRequest loginRequest);
}
