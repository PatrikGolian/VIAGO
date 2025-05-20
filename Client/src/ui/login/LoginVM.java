package ui.login;

import dtos.auth.LoginRequest;
import dtos.user.UserDataDto;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.exceptions.ValidationException;
import networking.authentication.AuthenticationClient;
import networking.user.BlacklistSubscriber;
import startup.ViewHandler;
import startup.ViewType;
import state.AppState;
import ui.popup.MessageType;
import utils.StringUtils;

import java.io.IOException;

public class LoginVM
{
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty passwordProp = new SimpleStringProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final BooleanProperty disableLoginButtonProp = new SimpleBooleanProperty(
      true);

  private final AuthenticationClient authService;

  public LoginVM(AuthenticationClient authService)
  {
    this.authService = authService;
    emailProp.addListener(this::updateLoginButtonState);
    passwordProp.addListener(this::updateLoginButtonState);

    try
    {
      new BlacklistSubscriber("localhost", 2910,
          this::blacklistPopupAndDisconnect);
    }
    catch (IOException e)
    {

    }
  }

  public void blacklistPopupAndDisconnect()
  {
    ViewHandler.showView(ViewType.LOGIN);
  }

  public StringProperty emailProperty()
  {
    return emailProp;
  }

  public StringProperty passwordProperty()
  {
    return passwordProp;
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public BooleanProperty enableLoginButtonProperty()
  {
    return disableLoginButtonProp;
  }

  private void updateLoginButtonState(Observable observable)
  {
    boolean shouldDisable =
        StringUtils.isNullOrEmpty(emailProp.get()) || StringUtils.isNullOrEmpty(
            passwordProp.get());

    disableLoginButtonProp.set(shouldDisable);
  }

  public void login()
  {
    // Clear previous messages
    messageProp.set("");
    String email = emailProp.get();
    String password = passwordProp.get();

    // --- HARD-CODED FORMAT VALIDATION ---
    if (email == null || email.trim().isEmpty())
    {
      messageProp.set("Email cannot be empty.");
      return;
    }

    if (!email.matches("^\\d{6,}@via\\.dk$") && !email.matches("^[a-zA-Z]+@via\\.dk$"))
    {
      messageProp.set("Email must be a valid VIA UC address (e.g., abcd@via.dk or 123456@via.dk).");
      return;
    }

    if (password == null || password.trim().isEmpty())
    {
      messageProp.set("Password cannot be empty.");
      return;
    }

    if (password.length() < 8)
    {
      messageProp.set("Password must be at least 8 characters.");
      return;
    }

    if (password.length() > 24)
    {
      messageProp.set("Password must be no more than 24 characters.");
      return;
    }

    // --- SERVER-SIDE VALIDATION ---
    LoginRequest loginRequest = new LoginRequest(email, password);

    try
    {
      UserDataDto user = authService.login(loginRequest);

      if (user == null)
      {
        messageProp.set("Login failed: No user returned from server.");
        return;
      }

      AppState.setCurrentUser(user);

      if (user.isAdmin())
      {
        ViewHandler.showView(ViewType.VIEWUSERS);
      }
      else
      {
        ViewHandler.showView(ViewType.STUDENTACCOUNT);
      }
    }
    catch (ValidationException ve)
    {
      // Includes "User not found", "Incorrect password", "Blacklisted", etc.
      messageProp.set(ve.getMessage());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      messageProp.set("Unexpected error: " + e.getMessage());
    }
  }
}
