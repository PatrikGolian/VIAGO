package networking.requesthandlers;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import networking.exceptions.InvalidActionException;
import networking.readerswriters.ReadWrite;
import services.authentication.AuthenticationService;

import java.sql.SQLException;

public class AuthRequestHandler implements RequestHandler
{
    private final AuthenticationService authenticationService;

    public AuthRequestHandler(AuthenticationService authenticationService, ReadWrite sharedResource)
    {
        this.authenticationService = authenticationService;
    }

    @Override
    public Object handle(String action, Object payload) throws SQLException
    {
        switch (action)
        {
            case "register" ->
            {
                authenticationService.registerUser((RegisterUserRequest) payload);
            }
            case "login" ->
            {
                return authenticationService.login((LoginRequest) payload);
            }
            default ->
            {
                throw new InvalidActionException("auth", action);
            }
        }
        return null;
    }
}
