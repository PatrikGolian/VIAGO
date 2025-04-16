package services.authentication;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.user.UserDataDto;

import java.sql.SQLException;

public interface AuthenticationService {
    void registerUser(RegisterUserRequest request) throws SQLException;
    UserDataDto login(LoginRequest request) throws SQLException;
}
