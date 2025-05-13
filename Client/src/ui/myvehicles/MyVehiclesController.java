package ui.myvehicles;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;
import ui.reservation.ReservationVM;
import ui.reservation.VehicleFx;

public class MyVehiclesController implements Controller
{
  @FXML private TableView<VehicleFx> vehiclesTable;
  @FXML private TableColumn<VehicleFx, String> typeColumn;
  @FXML private TableColumn<VehicleFx, String> brandColumn;
  @FXML private TableColumn<VehicleFx, String> modelColumn;
  @FXML private TableColumn<VehicleFx, Double> priceColumn;
  @FXML private TableColumn<VehicleFx, String> conditionColumn;
  @FXML private TableColumn<VehicleFx, String> colorColumn;
  @FXML private TableColumn<VehicleFx, String> stateColumn;

  @FXML private TextField bikeTypeField;
  @FXML private TextField speedField;
  @FXML private TextField rangeField;

  @FXML private Label bikeTypeLabel;
  @FXML private Label speedLabel;
  @FXML private Label rangeLabel;
  @FXML private Button addButton;

  @FXML private Rectangle profileShapeRedirect;
  @FXML private Label profileTextRedirect;

  private final MyVehiclesVM viewModel;

  public MyVehiclesController(MyVehiclesVM viewModel)
  {
    this.viewModel = viewModel;
  }

  public void initialize()
  {
    profileTextRedirect.textProperty().bindBidirectional(
        viewModel.profileTextRedirectProperty());

    vehiclesTable.setItems(viewModel.getVehicleList());
    vehiclesTable.setEditable(false);
    vehiclesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    viewModel.selectedVehicleProperty()
        .bind(vehiclesTable.getSelectionModel().selectedItemProperty());
    viewModel.selectedVehicleProperty().addListener((obs, oldValue, newV) -> {
      viewModel.setVisibility();
      if (newV != null)
      {
        viewModel.getIdProp().set(newV.idPropProperty().get());
        switch (newV.typePropProperty().get())
        {
          case "scooter" ->
          {
            viewModel.bikeTypeProperty().set("");
            viewModel.speedProperty().set(newV.maxSpeedPropProperty().get());
            viewModel.rangeProperty().set(newV.rangeProperty().get());
          }
          case "e-bike" ->
          {
            viewModel.bikeTypeProperty().set(newV.bikeTypePropProperty().get());
            viewModel.speedProperty().set(newV.maxSpeedPropProperty().get());
            viewModel.rangeProperty().set(newV.rangeProperty().get());
          }
          case "bike" ->
          {
            viewModel.speedProperty().set("");
            viewModel.rangeProperty().set("");
            viewModel.bikeTypeProperty().set(newV.bikeTypePropProperty().get());
          }
        }
      }
    });

    typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeProp"));
    brandColumn.setCellValueFactory(new PropertyValueFactory<>("brandProp"));
    modelColumn.setCellValueFactory(new PropertyValueFactory<>("modelProp"));
    priceColumn.setCellValueFactory(
        new PropertyValueFactory<>("pricePerDayProp"));
    conditionColumn.setCellValueFactory(new PropertyValueFactory<>("conditionProp"));
    colorColumn.setCellValueFactory(new PropertyValueFactory<>("colorProp"));
    stateColumn.setCellValueFactory(new PropertyValueFactory<>("stateProp"));

    addButton.setOnAction(e -> onAddButton());

    bikeTypeField.textProperty()
        .bindBidirectional(viewModel.bikeTypeProperty());
    speedField.textProperty().bindBidirectional(viewModel.speedProperty());
    rangeField.textProperty().bindBidirectional(viewModel.rangeProperty());


    //visibility
    speedLabel.visibleProperty().bind(viewModel.getSpeedLabelVisibility());
    speedField.visibleProperty().bind(viewModel.getSpeedFieldVisibility());
    rangeLabel.visibleProperty().bind(viewModel.getRangeLabelVisibility());
    rangeField.visibleProperty().bind(viewModel.getRangeFieldVisibility());
    bikeTypeLabel.visibleProperty()
        .bind(viewModel.getBikeTypeLabelVisibility());
    bikeTypeField.visibleProperty()
        .bind(viewModel.getBikeTypeFieldVisibility());
  }


  public void onAddButton()
  {
    ViewHandler.showView(ViewType.ADDNEW);
  }
}
