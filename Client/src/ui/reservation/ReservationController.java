package ui.reservation;

import javafx.scene.control.*;

import javafx.fxml.FXML;
import persistence.daos.vehicle.VehiclePostgresDao;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;

public class ReservationController implements Controller
{
  @FXML private TableView<VehicleFx> vehicleTable;
  @FXML private TableColumn<VehicleFx, String> typeColumn;
  @FXML private TableColumn<VehicleFx, String> brandColumn;
  @FXML private TableColumn<VehicleFx, String> modelColumn;
  @FXML private TableColumn<VehicleFx, Double> priceColumn;
  @FXML private TableColumn<VehicleFx, String> stateColumn;

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


 private final ReservationVM viewModel;

  public ReservationController(ReservationVM vm)
  {
    this.viewModel = vm;
  }

  public void initialize()
  {
    viewModel.loadVehicles();

    vehicleTable.setItems(viewModel.getVehicleList());
    vehicleTable.setEditable(false);
    vehicleTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    typeColumn.setCellValueFactory(param -> param.getValue().typePropProperty());
    brandColumn.setCellValueFactory(param -> param.getValue().brandPropProperty());
    modelColumn.setCellValueFactory(param -> param.getValue().modelPropProperty());
    priceColumn.setCellValueFactory(param -> param.getValue().pricePerDayPropProperty().asObject());
    stateColumn.setCellValueFactory(param -> param.getValue().statePropProperty());
  }

  public void onBack()
  {
    ViewHandler.showView(ViewType.WELCOME);
  }
}
