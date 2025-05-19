package ui.studentaccount;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import model.Date;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;
import ui.popup.MessageType;
import ui.reservation.ReservationFx;

public class StudentAccountController implements Controller
{
  @FXML Button logoutButton;
  @FXML Label changeNameLabel;
  @FXML Label changePasswordLabel;
  @FXML Label coverLabel;
  @FXML Label profileTextRedirect;
  @FXML Rectangle profileShapeRedirect;
  @FXML TextField firstNameField;
  @FXML TextField lastNameField;
  @FXML TextField oldPasswordField;
  @FXML TextField newPasswordField;
  @FXML TextField confirmPasswordField;
  @FXML Button confirmButton;
  @FXML Button editButton;
  @FXML private TableView<ReservationFx> reservationTable;
  @FXML private TableColumn<ReservationFx, String> typeColumn;
  @FXML private TableColumn<ReservationFx, Date> startDateColumn;
  @FXML private TableColumn<ReservationFx, Date> endDateColumn;
  @FXML private TableColumn<ReservationFx, Double> priceColumn;
  @FXML private TableColumn<ReservationFx, String> ownerEmail;
  @FXML Label firstLabel;
  @FXML Label lastLabel;
  @FXML Label oldLabel;
  @FXML Label newLabel;
  @FXML Label confirmLabel;
  @FXML Label messageLabel;
  @FXML Label nameLabel;
  @FXML Label emailLabel;
  @FXML Label rentRedirect;
  @FXML Label myVehiclesRedirect;

  private final StudentAccountVM viewModel;

  public StudentAccountController(StudentAccountVM viewModel)
  {
    this.viewModel = viewModel;
  }

  private void setFieldsAndLabels()
  {
    viewModel.resetInfo();
  }

  public void initialize()
  {
    // Bind fields and labels
    firstNameField.textProperty()
        .bindBidirectional(viewModel.firstNameProperty());
    lastNameField.textProperty()
        .bindBidirectional(viewModel.lastNameProperty());
    oldPasswordField.textProperty()
        .bindBidirectional(viewModel.oldPasswordProperty());
    newPasswordField.textProperty()
        .bindBidirectional(viewModel.newPasswordProperty());
    confirmPasswordField.textProperty()
        .bindBidirectional(viewModel.confirmPasswordProperty());
    emailLabel.textProperty().bind(viewModel.emailPropery());
    nameLabel.textProperty().bind(viewModel.nameProperty());
    messageLabel.textProperty().bind(viewModel.messageLabelProperty());

    // Set welcome labels, firs name, last name editable fiels

    // Fields and labels visibility
    firstNameField.visibleProperty()
        .bind(viewModel.firstNameFieldVisibilityProperty());
    firstLabel.visibleProperty()
        .bind(viewModel.firstNameLabelVisibilityProperty());
    lastNameField.visibleProperty()
        .bind(viewModel.lastNameFieldVisibilityProperty());
    lastLabel.visibleProperty()
        .bind(viewModel.lastNameLabelVisibilityProperty());
    oldPasswordField.visibleProperty()
        .bind(viewModel.oldPasswordFieldVisibilityProperty());
    oldLabel.visibleProperty()
        .bind(viewModel.oldPasswordLabelVisibilityProperty());
    newPasswordField.visibleProperty()
        .bind(viewModel.newPasswordFieldVisibilityProperty());
    newLabel.visibleProperty()
        .bind(viewModel.newPasswordLabelVisibilityProperty());
    confirmPasswordField.visibleProperty()
        .bind(viewModel.confirmPasswordFieldVisibilityProperty());
    confirmLabel.visibleProperty()
        .bind(viewModel.confirmPasswordLabelVisibilityProperty());
    changeNameLabel.visibleProperty()
        .bind(viewModel.changeNameLabelVisibility());
    changePasswordLabel.visibleProperty()
        .bind(viewModel.changePasswordLabelVisibility());
    confirmButton.visibleProperty().bind(viewModel.confirmButtonVisibility());

    setFieldsAndLabels();
    reservationTable.setItems(viewModel.getReservationList());
    reservationTable.setEditable(false);
    reservationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    // Table view
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeProp"));
    startDateColumn.setCellValueFactory(
        new PropertyValueFactory<>("startDateProp"));
    endDateColumn.setCellValueFactory(
        new PropertyValueFactory<>("endDateProp"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceProp"));
    ownerEmail.setCellValueFactory(
        new PropertyValueFactory<>("ownerEmailProp"));

    profileTextRedirect.textProperty().bindBidirectional(viewModel.profileTextRedirectProperty());
    coverLabel.textProperty().bindBidirectional(viewModel.coverLabelProperty());
    viewModel.setProfileInitials();
    // Sets editable fields' visibility
    viewModel.setVisibility();
  }

  public void onEditButton()
  {
    viewModel.toggleEditMode();
  }
  public void onConfirmButton(){viewModel.confirmEdit();}

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
  public void onDeleteReservation()
  {
    ReservationFx selected = reservationTable.getSelectionModel().getSelectedItem();
    if (selected != null)
    {
      viewModel.deleteReservation(selected);
      clearTable();
      viewModel.loadReservations();

    }
    else
    {
      ViewHandler.popupMessage(MessageType.WARNING, "Please select a reservation to delete.");
    }
  }

  public void onLogout()
  {
    ViewHandler.showView(ViewType.LOGIN);
  }

  public void clearTable() {
    reservationTable.getItems().clear();
  }
}
