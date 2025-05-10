package ui.login;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;

public class LoginController implements Controller
{
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

  public void onBack()
  {
    ViewHandler.showView(ViewType.WELCOME);
  }

  public void onLogin()
  {
    vm.login();
  }
}
