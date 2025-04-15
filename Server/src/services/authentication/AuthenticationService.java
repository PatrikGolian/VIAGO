package services.authentication;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.user.UserDataDto;

public interface AuthenticationService {
    void registerUser(RegisterUserRequest request);
    UserDataDto login(LoginRequest request);
}
