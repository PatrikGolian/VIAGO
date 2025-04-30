package ui.reservation;

import javafx.scene.control.*;

import javafx.fxml.FXML;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;

public class ReservationController implements Controller
{
  @FXML private TableView<ReservationFx> vehicleTable;
  @FXML private TableColumn<ReservationFx, String> typeColumn;
  @FXML private TableColumn<ReservationFx, String> brandColumn;
  @FXML private TableColumn<ReservationFx, String> modelColumn;
  @FXML private TableColumn<ReservationFx, Integer> priceColumn;
  @FXML private TableColumn<ReservationFx, String> stateColumn;

  @FXML private TextField conditionField;
  @FXML private TextField colorField;
  @FXML private TextField ownerField;
  @FXML private TextField bikeTypeField;
  @FXML private TextField speedField;
  @FXML private TextField rangeField;
  @FXML private Label bikeTypeLabel;
  @FXML private Label speedLabel;
  @FXML private Label rangeLabel;
  @FXML private Label priceField;
  @FXML private Button reserveButton;


 private final ReservationVM viewModel;

  public ReservationController(ReservationVM vm)
  {
    this.viewModel = vm;
  }

  public void initialize()
  {
    viewModel.loadVehicles;

  }

  public void onBack()
  {
    ViewHandler.showView(ViewType.WELCOME);
  }
}
