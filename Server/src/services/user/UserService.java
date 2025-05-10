package services.user;

import dtos.user.BlacklistUserRequest;
import dtos.user.PromoteUserRequest;
import dtos.user.UpdatePasswordRequest;
import dtos.user.ViewUsers;

import java.sql.SQLException;
import java.util.List;

public interface UserService
{
  void promoteToAdmin(PromoteUserRequest request) throws SQLException;

  void blacklistUser(BlacklistUserRequest request) throws SQLException;

  List<ViewUsers.UserDisplayDto> getUsersOverview(ViewUsers.Request request)
      throws SQLException;

  void updatePassword(UpdatePasswordRequest request) throws SQLException;
}
