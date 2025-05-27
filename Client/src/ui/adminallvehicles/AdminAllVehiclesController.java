package ui.adminallvehicles;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;
import ui.myvehicles.MyVehiclesVM;
import ui.popup.MessageType;
import ui.reservation.VehicleFx;

public class AdminAllVehiclesController implements Controller
{

  private final AdminAllVehiclesVM viewModel;

  private final StringProperty profileTextRedirectProp = new SimpleStringProperty();

  @FXML private TableView<VehicleFx> vehiclesTable;
  @FXML private TableColumn<VehicleFx, String> typeColumn;
  @FXML private TableColumn<VehicleFx, String> brandColumn;
  @FXML private TableColumn<VehicleFx, String> modelColumn;
  @FXML private TableColumn<VehicleFx, Double> priceColumn;
  @FXML private TableColumn<VehicleFx, String> conditionColumn;
  @FXML private TableColumn<VehicleFx, String> colorColumn;
  @FXML private TableColumn<VehicleFx, String> stateColumn;
  @FXML private TableColumn<VehicleFx, String> ownerEmailColumn;

  @FXML private TextField bikeTypeField;
  @FXML private TextField speedField;
  @FXML private TextField rangeField;

  @FXML private Label bikeTypeLabel;
  @FXML private Label speedLabel;
  @FXML private Label rangeLabel;
  @FXML Label messageLabel;
  @FXML private Button deleteButton;

  @FXML private Rectangle profileShapeRedirect;
  @FXML private Label profileTextRedirect;
  @FXML private TextField searchField;

  public AdminAllVehiclesController(AdminAllVehiclesVM viewModel)
  {
    this.viewModel = viewModel;
  }

  public void initialize()
  {
    profileTextRedirect.textProperty().bindBidirectional(
        viewModel.profileTextRedirectProperty());
    viewModel.setProfileInitials();

    vehiclesTable.setItems(viewModel.getVehicleList());
    vehiclesTable.setEditable(false);
    vehiclesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    bikeTypeField.setEditable(false);
    bikeTypeField.setFocusTraversable(false);

    speedField.setEditable(false);
    speedField.setFocusTraversable(false);

    rangeField.setEditable(false);
    rangeField.setFocusTraversable(false);

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
    ownerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("ownerProp"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDayProp"));
    conditionColumn.setCellValueFactory(new PropertyValueFactory<>("conditionProp"));
    colorColumn.setCellValueFactory(new PropertyValueFactory<>("colorProp"));
    stateColumn.setCellValueFactory(new PropertyValueFactory<>("stateProp"));

    deleteButton.setOnAction(e -> onDeleteVehicle());

    bikeTypeField.textProperty()
        .bindBidirectional(viewModel.bikeTypeProperty());
    speedField.textProperty().bindBidirectional(viewModel.speedProperty());
    rangeField.textProperty().bindBidirectional(viewModel.rangeProperty());

    messageLabel.textProperty().bind(viewModel.messageLabelProperty());

    //visibility
    speedLabel.visibleProperty().bind(viewModel.getSpeedLabelVisibility());
    speedField.visibleProperty().bind(viewModel.getSpeedFieldVisibility());
    rangeLabel.visibleProperty().bind(viewModel.getRangeLabelVisibility());
    rangeField.visibleProperty().bind(viewModel.getRangeFieldVisibility());
    bikeTypeLabel.visibleProperty()
        .bind(viewModel.getBikeTypeLabelVisibility());
    bikeTypeField.visibleProperty()
        .bind(viewModel.getBikeTypeFieldVisibility());

    // Search bar
    searchField.setOnKeyPressed(event -> {
      if (event.getCode().toString().equals("ENTER"))
      {
        String input = searchField.getText();
        String newVal = (input == null) ? "" : input.toLowerCase();

        viewModel.getFilteredVehicles().setPredicate(vehicle -> {
          String type = (vehicle.typePropProperty().get() != null) ?
              vehicle.typePropProperty().get().toLowerCase() :
              "";
          String condition = (vehicle.conditionPropProperty().get() != null) ?
              vehicle.conditionPropProperty().get().toLowerCase() :
              "";

          return type.contains(newVal) || condition.contains(newVal);
        });

        vehiclesTable.setItems(viewModel.getFilteredVehicles());
      }
    });
  }

  public void onDeleteVehicle()
  {
    VehicleFx selected = vehiclesTable.getSelectionModel().getSelectedItem();
    if (selected != null)
    {
      viewModel.deleteVehicle(selected);
      clearTable();
      viewModel.loadVehicles();

    }
    else
    {
      ViewHandler.popupMessage(MessageType.WARNING, "Please select a vehicle to delete.");
    }
  }
  public void clearTable() {
    vehiclesTable.getItems().clear();
  }

  public void onProfileRedirect()
  {
    ViewHandler.showView(ViewType.VIEWUSERS);
  }
}
