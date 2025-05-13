package networking.requesthandlers;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import networking.exceptions.InvalidActionException;
import networking.readerswriters.ReadWrite;
import networking.readerswriters.Reader;
import networking.readerswriters.Writer;
import services.authentication.AuthenticationService;

import java.sql.SQLException;

public class AuthRequestHandler implements RequestHandler
{
    private final AuthenticationService authenticationService;
    private final ReadWrite lock;

    public AuthRequestHandler(AuthenticationService authenticationService, ReadWrite sharedResource)
    {
        this.authenticationService = authenticationService;
        this.lock = sharedResource;
    }

    @Override
    public Object handle(String action, Object payload) throws SQLException
    {
        switch (action)
        {
            case "register" ->
            {
                Writer writer = new Writer(lock, () -> {
                    try {
                        authenticationService.registerUser((RegisterUserRequest) payload);
                    } catch (SQLException e) {
                        throw new RuntimeException(e); // wrap checked exception
                    }
                });
                Thread writerThread = new Thread(writer);
                writerThread.start();
                try {
                    writerThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            case "login" ->
            {
                Reader<Object> reader = new Reader<>(lock, () -> {
                    try
                    {
                        return authenticationService.login(
                            (LoginRequest) payload);
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException(e); // wrap checked exception
                    }
                });
                Thread readerThread = new Thread(reader);
                readerThread.start();
                try
                {
                    readerThread.join();
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
                return reader.getResult();
            }
            default ->
            {
                throw new InvalidActionException("auth", action);
            }
        }
        return null;
    }
}
