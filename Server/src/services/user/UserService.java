package services.user;

import dtos.user.BlacklistUserRequest;
import dtos.user.PromoteUserRequest;
import dtos.user.UpdatePasswordRequest;
import dtos.user.ViewUsers;

import java.util.List;

public interface UserService
{
    void promoteToAdmin(PromoteUserRequest request);

    void blacklistUser(BlacklistUserRequest request);

    List<ViewUsers.UserDisplayDto> getUsersOverview(ViewUsers.Request request);

    void updatePassword(UpdatePasswordRequest request);
}
