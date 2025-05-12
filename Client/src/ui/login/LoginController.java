package ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;

public class LoginController implements Controller
{
  @FXML public Label registerTextRedirect;
  public Label messageLabel;
  public PasswordField passwordInput;
  public TextField emailInput;
  public Button buttonLogin;

  private final LoginVM vm;

  public LoginController(LoginVM vm)
  {
    this.vm = vm;
  }

  public void initialize()
  {
    messageLabel.textProperty().bind(vm.messageProperty());
    emailInput.textProperty().bindBidirectional(vm.emailProperty());
    passwordInput.textProperty().bindBidirectional(vm.passwordProperty());
    buttonLogin.disableProperty().bind(vm.enableLoginButtonProperty());
  }

  public void onRegisterText()
  {
    ViewHandler.showView(ViewType.REGISTER);
  }

  public void onLogin()
  {
    vm.login();
  }
}
