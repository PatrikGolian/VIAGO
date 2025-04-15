package ui.register;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;

public class RegisterController implements Controller
{
    @FXML
    private TextField emailInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private PasswordField repeatPasswordInput;
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField lastNameInput;
    @FXML
    private Label messageLabel;
    @FXML
    private Button buttonRegister;

    private final RegisterVM viewModel;

    public RegisterController(RegisterVM vm)
    {
        this.viewModel = vm;
    }

    public void initialize()
    {
        emailInput.textProperty().bindBidirectional(viewModel.emailProperty());
        passwordInput.textProperty().bindBidirectional(viewModel.passwordProperty());
        repeatPasswordInput.textProperty().bindBidirectional(viewModel.repeatProperty());
        firstNameInput.textProperty().bindBidirectional(viewModel.firstNameProperty());
        lastNameInput.textProperty().bindBidirectional(viewModel.lastNameProperty());

        messageLabel.textProperty().bind(viewModel.messageProperty());

        buttonRegister.disableProperty().bind(viewModel.disableRegisterButtonProperty());
    }

    public void onRegister()
    {
        viewModel.registerUser();
    }

    public void onBack()
    {
        ViewHandler.showView(ViewType.WELCOME);
    }

}
