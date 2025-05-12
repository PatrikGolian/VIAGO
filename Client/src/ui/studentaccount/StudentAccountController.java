package ui.studentaccount;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Date;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;
import ui.reservation.ReservationFx;
import ui.reservation.VehicleFx;

public class StudentAccountController implements Controller
{
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
  @FXML Label nameLabel;
  @FXML Label emailLabel;
  @FXML Label rentRedirect;

  private final StudentAccountVM viewModel;

  public StudentAccountController(StudentAccountVM viewModel)
  {
    this.viewModel = viewModel;
  }

  public void initialize()
  {
    //bind fields and labels
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

    //hide elements for edit


    // Table view
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeProp"));
    startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDateProp"));
    endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDateProp"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceProp"));
    ownerEmail.setCellValueFactory(new PropertyValueFactory<>("ownerEmailProp"));
  }

  public void onRentRedirect()
  {
    ViewHandler.showView(ViewType.RESERVATION);
  }
}
