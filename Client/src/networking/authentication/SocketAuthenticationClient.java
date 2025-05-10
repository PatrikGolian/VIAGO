package networking.authentication;

import dtos.Request;
import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.user.UserDataDto;
import networking.SocketService;

public class SocketAuthenticationClient implements AuthenticationClient
{
  @Override public void registerUser(RegisterUserRequest user)
  {
    Request request = new Request("auth", "register", user);
    SocketService.sendRequest(request);

  }

  @Override public UserDataDto login(LoginRequest loginRequest)
  {
    Request request = new Request("auth", "login", loginRequest);
    UserDataDto userData = (UserDataDto) SocketService.sendRequest(request);
    return userData;
  }
}
