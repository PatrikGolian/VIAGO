package networking.user;

import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.BlacklistUserRequest;
import dtos.user.UserDataDto;

import java.sql.SQLException;
import java.util.List;

public interface UsersClient
{
    List<UserDataDto> getUsers();
    String getBlackListReason(BlacklistUserRequest request);
    void blacklist(BlacklistUserRequest request);
    void blackListReason(BlacklistUserRequest request);
    void changeUser(ChangeUserRequest request) throws SQLException;
    String getPassword(GetPasswordRequest request) throws SQLException;
}
