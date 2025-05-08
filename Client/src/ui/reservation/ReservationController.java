package ui.reservation;

import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.vehicles.Vehicle;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;

public class ReservationController implements Controller
{
  @FXML private TableView<VehicleFx> vehicleTable;
  @FXML private TableColumn<Vehicle, String> typeColumn;
  @FXML private TableColumn<Vehicle, String> brandColumn;
  @FXML private TableColumn<Vehicle, String> modelColumn;
  @FXML private TableColumn<Vehicle, Double> priceColumn;
  @FXML private TableColumn<Vehicle, String> stateColumn;

  @FXML private TextField conditionField;
  @FXML private TextField colorField;
  @FXML private TextField ownerField;
  @FXML private TextField bikeTypeField;
  @FXML private TextField speedField;
  @FXML private TextField rangeField;
  @FXML private Label bikeTypeLabel;
  @FXML private Label speedLabel;
  @FXML private Label rangeLabel;
  @FXML private TextField priceField;
  @FXML private Button reserveButton;
  @FXML private Button backButton;


 private final ReservationVM viewModel;

  public ReservationController(ReservationVM vm)
  {
    this.viewModel = vm;
  }

  public void initialize()
  {
    vehicleTable.setItems(viewModel.getVehicleList());
    vehicleTable.setEditable(false);
    vehicleTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeProp"));
    brandColumn.setCellValueFactory(new PropertyValueFactory<>("brandProp"));
    modelColumn.setCellValueFactory(new PropertyValueFactory<>("modelProp"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDayProp"));
    stateColumn.setCellValueFactory(new PropertyValueFactory<>("stateProp"));

    backButton.setOnAction(e -> onBackButton());

    //conditionField.textProperty().bind();
  }

  public void onBackButton()
  {
    ViewHandler.showView(ViewType.WELCOME);
  }
}
