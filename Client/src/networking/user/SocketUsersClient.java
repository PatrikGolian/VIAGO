package networking.user;

import dtos.Request;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.BlacklistUserRequest;
import dtos.user.PromoteUserRequest;
import dtos.user.UserDataDto;
import dtos.user.ViewUsers;
import javafx.collections.ObservableList;
import networking.SocketService;

import java.util.List;

public class SocketUsersClient implements UsersClient
{
    @Override
    public List<UserDataDto> getUsers()
    {
        Request request = new Request("users", "view_users", null);
        return (List<UserDataDto>) SocketService.sendRequest(request);
    }

    @Override
    public void promoteUser(PromoteUserRequest promoteRequest)
    {
        Request request = new Request("users", "promote", promoteRequest);
        SocketService.sendRequest(request);
    }

    @Override
    public void blacklist(BlacklistUserRequest blacklistRequest)
    {
        Request request = new Request("users", "blacklist", blacklistRequest);
        SocketService.sendRequest(request);
    }

    @Override public void changeUser(ChangeUserRequest user)
    {
        Request request = new Request("users", "changeUser", user);
        SocketService.sendRequest(request);

    }

    @Override public String getPassword(GetPasswordRequest email)
    {
        Request request = new Request("users","getPassword", email);

        return (String) SocketService.sendRequest(request);
    }
}
