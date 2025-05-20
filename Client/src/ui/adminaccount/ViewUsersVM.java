package ui.adminaccount;

import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.BlacklistUserRequest;
import dtos.user.UserDataDto;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.user.UsersClient;
import networking.user.UsersSubscriber;
import startup.ViewHandler;
import state.AppState;
import ui.popup.MessageType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewUsersVM
{
  // Services and Data Models
  private final UsersClient userService;
  private final ObservableList<UserFx> users = FXCollections.observableArrayList();
  private final ObjectProperty<UserFx> selectedUser = new SimpleObjectProperty<>();
  private UserFx selected;

  // 2. UI State Properties
  // Button/Action State
  private final BooleanProperty showBlacklistButtonProp = new SimpleBooleanProperty();
  private final BooleanProperty disableChangePasswordButtonProp = new SimpleBooleanProperty(
      true);

  // Visibility of Labels and Fields
  private final BooleanProperty changeNameLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty changePasswordLabelVisibility = new SimpleBooleanProperty();

  private final BooleanProperty firstNameFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty lastNameFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty oldPasswordFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty newPasswordFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty confirmPasswordFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty blackListReasonFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty confirmButtonVisibility = new SimpleBooleanProperty();

  private final BooleanProperty firstNameLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty lastNameLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty oldPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty newPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty confirmPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty blackListReasonLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty messageLabelVisibility = new SimpleBooleanProperty();

  // User Editable Input Properties
  private final StringProperty firstNameProp = new SimpleStringProperty();
  private final StringProperty lastNameProp = new SimpleStringProperty();
  private final StringProperty oldPasswordProp = new SimpleStringProperty();
  private final StringProperty newPasswordProp = new SimpleStringProperty();
  private final StringProperty confirmPasswordProp = new SimpleStringProperty();
  private final StringProperty blackListedReasonFieldProp = new SimpleStringProperty();

  // Informational Display Properties
  private final StringProperty profileTextRedirectProperty = new SimpleStringProperty();
  private final StringProperty coverLabelProperty = new SimpleStringProperty();
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty nameProp = new SimpleStringProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty messageProp2 = new SimpleStringProperty();
  private final StringProperty blackListButtonName = new SimpleStringProperty();

  public ViewUsersVM(UsersClient userService)
  {
    this.userService = userService;
    try
    {
      new UsersSubscriber("localhost", 2910, () -> loadUsers());
    }
    catch (IOException e)
    {
    }
    showBlacklistButtonProp.set(AppState.getCurrentUser().isAdmin());
  }

  // Initialization / Setup
  public void setSelected(UserFx selectedUser)
  {
    selected = selectedUser;
  }

  public void setProfileInitials()
  {
    String firstname = AppState.getCurrentUser().firstName();
    String lastname = AppState.getCurrentUser().lastName();
    profileTextRedirectProperty.set(
        "" + firstname.charAt(0) + lastname.charAt(0));
    coverLabelProperty.set("" + firstname.charAt(0) + lastname.charAt(0));
  }

  public void setVisibilityEditFields()
  {
    messageLabelVisibility.set(false);
    confirmPasswordLabelVisibility.set(false);
    confirmPasswordFieldVisibility.set(false);

    firstNameLabelVisibility.set(false);
    firstNameFieldVisibility.set(false);

    lastNameLabelVisibility.set(false);
    lastNameFieldVisibility.set(false);

    oldPasswordLabelVisibility.set(false);
    oldPasswordFieldVisibility.set(false);

    newPasswordLabelVisibility.set(false);
    newPasswordFieldVisibility.set(false);

    changeNameLabelVisibility.set(false);
    changePasswordLabelVisibility.set(false);
  }

  public void setVisibility()
  {
    UserFx userFx = selectedUser.get();
    if (userFx == null)
    {
      return;
    }
    else if (userFx.isBlacklistedProperty().get() == true)
    {
      {
        blackListReasonFieldVisibility.set(true);
        blackListReasonLabelVisibility.set(true);
      }
    }
    else
    {
      blackListReasonLabelVisibility.set(false);
      blackListReasonFieldVisibility.set(false);
    }
  }

  public void toggleEditMode()
  {
    boolean currentlyVisible = firstNameFieldVisibility.get(); // Use any field's visibility
    boolean newVisibility = !currentlyVisible;

    confirmPasswordLabelVisibility.set(newVisibility);
    confirmPasswordFieldVisibility.set(newVisibility);

    firstNameLabelVisibility.set(newVisibility);
    firstNameFieldVisibility.set(newVisibility);

    lastNameLabelVisibility.set(newVisibility);
    lastNameFieldVisibility.set(newVisibility);

    oldPasswordLabelVisibility.set(newVisibility);
    oldPasswordFieldVisibility.set(newVisibility);

    newPasswordLabelVisibility.set(newVisibility);
    newPasswordFieldVisibility.set(newVisibility);

    changePasswordLabelVisibility.set(newVisibility);
    changeNameLabelVisibility.set(newVisibility);

    messageLabelVisibility.set(newVisibility);
    confirmButtonVisibility.set(newVisibility);
  }

  public void resetInfo()
  {
    AppState.setCurrentUser(AppState.getCurrentUser());

    String firstName = AppState.getCurrentUser().firstName();
    String lastName = AppState.getCurrentUser().lastName();

    nameProp.set(firstName + " " + lastName);
    emailProp.set(AppState.getCurrentUser().email());
    firstNameProp.set(firstName);
    lastNameProp.set(lastName);
    oldPasswordProp.set("");
    newPasswordProp.set("");
    confirmPasswordProp.set("");
    blackListButtonName.set("Blacklist");
  }

  private void updateEnableChangePasswordButton(Observable observable)
  {
    UserFx userFx = selected;
    boolean shouldDisable = !userFx.emailProperty().get()
        .equals(AppState.getCurrentUser().email());
    disableChangePasswordButtonProp.set(shouldDisable);
  }

  // User Account Management
  public void loadUsers()
  {
    try
    {
      List<UserDataDto> loadedUsers = userService.getUsers();
      users.clear();
      for (UserDataDto user : loadedUsers)
      {
        if (user.isAdmin() == false)
        {
          users.add(new UserFx(user));
        }
      }
    }
    catch (Exception e)
    {
      ViewHandler.popupMessage(MessageType.ERROR, e.getMessage());
    }
  }

  public void blacklist(UserFx user)
  {

    if (user.isBlacklistedProperty().getValue())
    {
      try
      {
        user.isBlacklistedProperty().set(false);
        BlacklistUserRequest request = new BlacklistUserRequest(
            user.emailProperty().get(), "");
        userService.blacklist(request);
        ViewHandler.popupMessage(MessageType.SUCCESS,
            user.firstNameProperty().get() + " has been unblacklisted!");
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
    else
    {
      try
      {
        user.isBlacklistedProperty().set(true);
        BlacklistUserRequest request = new BlacklistUserRequest(
            user.emailProperty().get(), "");
        userService.blacklist(request);
        ViewHandler.popupMessage(MessageType.REASON, "", user, userService);
      }
      catch (Exception e)
      {
        ViewHandler.popupMessage(MessageType.ERROR, e.getMessage());
      }
    }
  }

  public void blackListButtonName(boolean isBlacklisted)
  {
    if (isBlacklisted)
    {
      blackListButtonName.set("Remove Blacklist");
    }
    else
    {
      blackListButtonName.set("Blacklist User");
    }
  }

  public void blackListFieldSet(String reason)
  {
    blackListedReasonFieldProperty().set(reason);
  }

  public String getBlacklistReason(UserFx user)
  {
    BlacklistUserRequest request = new BlacklistUserRequest(
        user.emailProperty().get(), "");
    return userService.getBlackListReason(request);
  }

  // User Edit / Validation
  public void confirmEdit() throws SQLException
  {
    validateInfo();
    resetInfo();
  }

  private void validateInfo() throws SQLException
  {
    String first = firstNameProp.get().trim();
    String last = lastNameProp.get().trim();

    // Name validation (always)
    if (first.isEmpty())
    {
      messageProp.set("First name cannot be empty");
      return;
    }
    if (first.length() <= 2 || !first.matches("[a-zA-Z ]+"))
    {
      messageProp.set("First name must be at least 3 letters and only letters");
      return;
    }
    if (last.isEmpty())
    {
      messageProp.set("Last name cannot be empty");
      return;
    }
    if (last.length() <= 2 || !last.matches("[a-zA-Z ]+"))
    {
      messageProp.set("Last name must be at least 3 letters and only letters");
      return;
    }

    // Password branch (only if they filled in “newPassword”)
    String finalPassword;
    boolean wantsPasswordChange =
        newPasswordProp.get() != null && !newPasswordProp.get().isBlank();

    if (wantsPasswordChange)
    {
      String oldPwd = oldPasswordProp.get();
      String newPwd = newPasswordProp.get();
      String confirmPwd = confirmPasswordProp.get();

      // Must supply old
      if (oldPwd == null || oldPwd.isBlank())
      {
        messageProp.set("Please enter your old password to change it");
        return;
      }
      // Verify old matches what’s on the server
      String oldPwds = oldPasswordProp.get();
      String currentOnServerU = getOldPassword(
          new GetPasswordRequest(emailProp.get()));

      System.out.println("Typed oldPwd = [" + oldPwds + "]");
      System.out.println("Server password = [" + currentOnServerU + "]");
      String currentOnServer = getOldPassword(
          new GetPasswordRequest(emailProp.get()));
      if (!oldPwd.equals(currentOnServer))
      {
        messageProp.set("Old password is not correct");
        return;
      }
      // new/confirm must match
      if (!newPwd.equals(confirmPwd))
      {
        messageProp.set("New passwords do not match");
        return;
      }
      finalPassword = newPwd;
    }
    else
    {
      // No password change requested -> keep the existing
      finalPassword = getOldPassword(new GetPasswordRequest(emailProp.get()));
    }

    // Send a single ChangeUserRequest with the name + finalPassword
    try
    {
      ChangeUserRequest req = new ChangeUserRequest(first, last,
          emailProp.get(), finalPassword);
      changeUser(req);
      ChangeUserRequest request = new ChangeUserRequest(first, last,
          emailProp.get(), finalPassword);
      changeUser(request);

      var oldDto = AppState.getCurrentUser();
      var updatedDto = new UserDataDto(oldDto.email(), first, last,
          oldDto.isBlacklisted(), oldDto.isAdmin(), oldDto.blackListReason());
      AppState.setCurrentUser(updatedDto);

      resetInfo();
      messageProp.set("Success!");
    }
    catch (Exception ex)
    {
      messageProp.set("Update failed: " + ex.getMessage());
    }
  }

  private void changeUser(ChangeUserRequest request) throws SQLException
  {
    userService.changeUser(request);
  }

  // Properties

  public BooleanProperty showBlacklistButtonProperty()
  {
    return showBlacklistButtonProp;
  }

  public BooleanProperty disableChangePasswordButtonProperty()
  {
    return disableChangePasswordButtonProp;
  }

  public ObservableList<UserFx> getUsersList()
  {
    return users;
  }

  private String getOldPassword(GetPasswordRequest request) throws SQLException
  {
    return userService.getPassword(request);
  }

  public ObjectProperty<UserFx> selectedUserProperty()
  {
    return selectedUser;
  }

  // Editable fields
  public Property<String> firstNameProperty()
  {
    return firstNameProp;
  }

  public Property<String> lastNameProperty()
  {
    return lastNameProp;
  }

  public Property<String> oldPasswordProperty()
  {
    return oldPasswordProp;
  }

  public Property<String> newPasswordProperty()
  {
    return newPasswordProp;
  }

  public Property<String> confirmPasswordProperty()
  {
    return confirmPasswordProp;
  }

  public BooleanProperty confirmButtonVisibility()
  {
    return confirmButtonVisibility;
  }

  public Property<String> blackListButtonNameProperty()
  {
    return blackListButtonName;
  }

  public Property<String> messageLabelProperty()
  {
    return messageProp;
  }

  // Visibilty of editable fields
  public BooleanProperty firstNameFieldVisibilityProperty()
  {
    return firstNameFieldVisibility;
  }

  public BooleanProperty firstNameLabelVisibilityProperty()
  {
    return firstNameLabelVisibility;
  }

  public BooleanProperty lastNameFieldVisibilityProperty()
  {
    return lastNameFieldVisibility;
  }

  public BooleanProperty getBlackListLabelVisibility()
  {
    return blackListReasonLabelVisibility;
  }

  public BooleanProperty getBlackListFieldVisibility()
  {
    return blackListReasonFieldVisibility;
  }

  public BooleanProperty lastNameLabelVisibilityProperty()
  {
    return lastNameLabelVisibility;
  }

  public BooleanProperty oldPasswordFieldVisibilityProperty()
  {
    return oldPasswordFieldVisibility;
  }

  public BooleanProperty oldPasswordLabelVisibilityProperty()
  {
    return oldPasswordLabelVisibility;
  }

  public BooleanProperty newPasswordFieldVisibilityProperty()
  {
    return newPasswordFieldVisibility;
  }

  public BooleanProperty newPasswordLabelVisibilityProperty()
  {
    return newPasswordLabelVisibility;
  }

  public BooleanProperty confirmPasswordFieldVisibilityProperty()
  {
    return confirmPasswordFieldVisibility;
  }

  public BooleanProperty confirmPasswordLabelVisibilityProperty()
  {
    return confirmPasswordLabelVisibility;
  }

  public BooleanProperty messageLabelVisibilityProperty()
  {
    return messageLabelVisibility;
  }

  public Property<String> emailPropery()
  {
    return emailProp;
  }

  public Property<String> nameProperty()
  {
    return nameProp;
  }

  public BooleanProperty changeNameLabelVisibility()
  {
    return changeNameLabelVisibility;
  }

  public BooleanProperty changePasswordLabelVisibility()
  {
    return changePasswordLabelVisibility;
  }

  public StringProperty profileTextRedirectProperty()
  {
    return profileTextRedirectProperty;
  }

  public StringProperty coverLabelProperty()
  {
    return coverLabelProperty;
  }

  public StringProperty blackListedReasonFieldProperty()
  {
    return blackListedReasonFieldProp;
  }
}



