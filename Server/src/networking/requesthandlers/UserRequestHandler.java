package networking.requesthandlers;

import dtos.auth.RegisterUserRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.BlacklistUserRequest;
import dtos.user.PromoteUserRequest;
import dtos.user.UpdatePasswordRequest;
import dtos.user.ViewUsers;
import networking.readerswriters.ReadWrite;
import networking.readerswriters.Reader;
import networking.readerswriters.Writer;
import services.user.UserService;

import java.sql.SQLException;

public class UserRequestHandler implements RequestHandler
{
    private final UserService userService;
    private final ReadWrite lock;

    public UserRequestHandler(UserService userService, ReadWrite sharedResource)
    {
        this.userService = userService;
        this.lock = sharedResource;
    }

    @Override
    public Object handle(String action, Object payload) throws SQLException
    {
        switch (action)
        {
            case "blacklist" -> {
                Writer writer = new Writer(lock, () -> {
                    try {
                        userService.blacklistUser((BlacklistUserRequest) payload);
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
            case "view_users" ->
            {
                Reader<Object> reader = new Reader<>(lock, () -> {
                    try
                    {
                        return userService.getUsersOverview();
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                });
                Thread thread = new Thread(reader);
                thread.start();
                try
                {
                    thread.join();
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
                return reader.getResult();

            }
            case "update_password" -> {
                Writer writer = new Writer(lock, () -> {
                    try {
                        userService.updatePassword((UpdatePasswordRequest) payload);
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
            case "changeUser" -> {
                Reader<Object> reader = new Reader<>(lock, () -> {
                    try
                    {
                        userService.changeUser((ChangeUserRequest) payload);
                        return Boolean.TRUE;
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                });
                Thread thread = new Thread(reader);
                thread.start();
                try
                {
                    thread.join();
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
                return reader.getResult();

            }
            case "getPassword" -> {
                Reader<Object> reader = new Reader<>(lock, () -> {
                    try
                    {
                        return userService.getPassword((GetPasswordRequest) payload);
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                });
                Thread thread = new Thread(reader);
                thread.start();
                try
                {
                    thread.join();
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
                return reader.getResult();
            }
        }
        return null;
    }
}