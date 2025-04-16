package networking.requesthandlers;

import dtos.user.BlacklistUserRequest;
import dtos.user.PromoteUserRequest;
import dtos.user.UpdatePasswordRequest;
import dtos.user.ViewUsers;
import services.user.UserService;

import java.sql.SQLException;

public class UserRequestHandler implements RequestHandler
{
    private final UserService userService;

    public UserRequestHandler(UserService userService)
    {
        this.userService = userService;
    }

    @Override
    public Object handle(String action, Object payload) throws SQLException
    {
        switch (action)
        {
            case "blacklist" -> userService.blacklistUser((BlacklistUserRequest) payload);
            case "promote" -> userService.promoteToAdmin((PromoteUserRequest) payload);
            case "view_users" ->
            {
                return userService.getUsersOverview((ViewUsers.Request) payload);
            }
            case "update_password" -> userService.updatePassword((UpdatePasswordRequest) payload);
        }
        return null; // just a default return value. Some actions above may return stuff.
    }
}