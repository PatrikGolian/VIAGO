package ui.addnew;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddNewController implements Controller
{
  @FXML Label rentRedirect;
  @FXML Label myVehiclesRedirect;
  @FXML Rectangle profileShapeRedirect;
  @FXML Label profileTextRedirect;
  @FXML TextField typeField;
  @FXML TextField brandField;
  @FXML TextField modelField;
  @FXML TextField conditionField;
  @FXML TextField colorField;
  @FXML TextField priceField;
  @FXML TextField speedField;
  @FXML TextField rangeField;
  @FXML TextField bikeTypeField;
  @FXML Label speedLabel;
  @FXML Label rangeLabel;
  @FXML Label bikeTypeLabel;
  @FXML Label messageLabel;
  @FXML Button addButton;

  private final AddNewVM viewModel;

  public AddNewController(AddNewVM vm)
  {
    this.viewModel = vm;
  }

  public void onTypeField()
  {
    viewModel.setVisibility();
  }

  public void initialize()
  {
    typeField.textProperty().bindBidirectional(viewModel.typeProperty());
    brandField.textProperty().bindBidirectional(viewModel.brandProperty());
    modelField.textProperty().bindBidirectional(viewModel.modelProperty());
    conditionField.textProperty()
        .bindBidirectional(viewModel.conditionProperty());
    colorField.textProperty().bindBidirectional(viewModel.colorProperty());
    priceField.textProperty().bindBidirectional(viewModel.priceProperty());
    speedField.textProperty().bindBidirectional(viewModel.speedProperty());
    rangeField.textProperty().bindBidirectional(viewModel.rangeProperty());
    bikeTypeField.textProperty()
        .bindBidirectional(viewModel.bikeTypeProperty());

    speedLabel.visibleProperty().bind(viewModel.getSpeedLabelVisibility());
    speedField.visibleProperty().bind(viewModel.getSpeedFieldVisibility());
    rangeLabel.visibleProperty().bind(viewModel.getRangeLabelVisibility());
    rangeField.visibleProperty().bind(viewModel.getRangeFieldVisibility());
    bikeTypeLabel.visibleProperty()
        .bind(viewModel.getBikeTypeLabelVisibility());
    bikeTypeField.visibleProperty()
        .bind(viewModel.getBikeTypeFieldVisibility());

    profileTextRedirect.textProperty().bindBidirectional(
        viewModel.profileTextRedirectProperty());
    viewModel.setProfileInitials();

    messageLabel.textProperty().bind(viewModel.messageProperty());

    addButton.disableProperty().bind(viewModel.enableAddButtonProperty());
  }

  public void onBack()
  {
    ViewHandler.showView(ViewType.WELCOME);
  }

  public void onAddButton()
  {
    viewModel.add();
  }

  public void onRentRedirect()
  {
    ViewHandler.showView(ViewType.RESERVATION);
  }

  public void onMyVehiclesRedirect()
  {
    ViewHandler.showView(ViewType.MYVEHICLES);
  }

  public void onProfileRedirect()
  {
    ViewHandler.showView(ViewType.STUDENTACCOUNT);
  }
}
