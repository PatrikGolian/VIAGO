package services.user;

import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.*;

import java.sql.SQLException;
import java.util.List;

public interface UserService
{
  void blacklistUser(BlacklistUserRequest request) throws SQLException;
  void blacklistUserReason(BlacklistUserRequest request) throws SQLException;

  String getBlackListReason(BlacklistUserRequest request) throws SQLException;

  List<UserDataDto> getUsersOverview()
      throws SQLException;

  void updatePassword(UpdatePasswordRequest request) throws SQLException;

  void changeUser(ChangeUserRequest request) throws SQLException;
  String getPassword(GetPasswordRequest request) throws SQLException;
}
