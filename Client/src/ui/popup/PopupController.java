package ui.popup;

import dtos.user.BlacklistUserRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.User;
import networking.user.UsersClient;
import services.user.UserService;
import startup.ViewHandler;
import ui.adminaccount.UserFx;

public class PopupController
{
  private final Stage stage;
  private final MessageType type;
  private final String message;
  public Label messageLabel;
  public Label errorLabel;
  public Label warningLabel;
  public Label successLabel;
  public TextField reasonField;
  private boolean reasonStated;
  @FXML private Button closeButton;

  private UsersClient userService;
  private UserFx user;

  public PopupController(Stage stage, MessageType type, String message)
  {
    this.stage = stage;
    this.type = type;
    this.message = message;
  }

  public PopupController(Stage stage, MessageType type, String message, UserFx user, UsersClient userService)
  {
    this.stage = stage;
    this.type = type;
    this.message = message;
    this.user = user;
    this.userService = userService;
  }

  public void initialize()
  {
    messageLabel.setText(message);
    errorLabel.setVisible(false);
    warningLabel.setVisible(false);
    successLabel.setVisible(false);
    reasonField.setVisible(false);
    reasonStated = false;
    closeButton.setText("Close");

    switch (type)
    {
      case ERROR -> errorLabel.setVisible(true);
      case SUCCESS -> successLabel.setVisible(true);
      case WARNING -> warningLabel.setVisible(true);
      case  REASON -> {
        closeButton.setText("Confirm");
        reasonField.setVisible(true);
        reasonStated = true;
      }
    }
  }

  public void onClose()
  {
    stage.close();
    if (reasonStated)
    {
      if (reasonField.getText() == null||reasonField.getText().isEmpty())
      {
        BlacklistUserRequest request = new BlacklistUserRequest(
            user.emailProperty().get(), "");
        userService.blackListReason(request);
        ViewHandler.popupMessage(MessageType.SUCCESS,
            user.firstNameProperty().get() + " has been blacklisted!");
      }else
      {
        BlacklistUserRequest request = new BlacklistUserRequest(
            user.emailProperty().get(), reasonField.getText());
        userService.blackListReason(request);
        user.isBlacklistedProperty().set(true);
        ViewHandler.popupMessage(MessageType.SUCCESS,
            user.firstNameProperty().get() + " has been blacklisted!");

      }
    }
  }
}
