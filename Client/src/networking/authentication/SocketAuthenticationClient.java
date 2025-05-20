package networking.authentication;

import dtos.Request;
import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.user.UserDataDto;
import model.exceptions.ValidationException;
import networking.SocketService;

public class SocketAuthenticationClient implements AuthenticationClient
{
  @Override public void registerUser(RegisterUserRequest user) throws ValidationException
  {
      Request request = new Request("auth", "register", user);
      SocketService.sendRequest(request);
  }

  @Override public UserDataDto login(LoginRequest loginRequest)
  {
    Request request = new Request("auth", "login", loginRequest);
    return (UserDataDto) SocketService.sendRequest(request);
  }
}
