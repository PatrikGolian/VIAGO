package ui.popup;

import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopupController
{
  private final Stage stage;
  private final MessageType type;
  private final String message;
  public Label messageLabel;
  public Label errorLabel;
  public Label warningLabel;
  public Label successLabel;

  public PopupController(Stage stage, MessageType type, String message)
  {
    this.stage = stage;
    this.type = type;
    this.message = message;
  }

  public void initialize()
  {
    messageLabel.setText(message);

    errorLabel.setVisible(false);
    warningLabel.setVisible(false);
    successLabel.setVisible(false);

    switch (type)
    {
      case ERROR -> errorLabel.setVisible(true);
      case SUCCESS -> successLabel.setVisible(true);
      case WARNING -> warningLabel.setVisible(true);
    }
  }

  public void onClose()
  {
    stage.close();
  }
}
