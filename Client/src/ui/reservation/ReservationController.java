package ui.reservation;

import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

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
  @FXML private TextField priceField;
  @FXML private TextField searchField;
  @FXML private Label bikeTypeLabel;
  @FXML private Label speedLabel;
  @FXML private Label rangeLabel;
  @FXML private Button reserveButton;
  @FXML private Button backButton;
  @FXML private Label conditionLabel;
  @FXML private Label colorLabel;
  @FXML private Label ownerLabel;
  @FXML private Label priceLabel;
  @FXML private Label datePickerLabel;

  @FXML private DatePicker datePicker;

  // Added Richard
  private DateCell iniCell = null;
  private DateCell endCell = null;

  private LocalDate iniDate;
  private LocalDate endDate;
  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.uuuu",
      Locale.ENGLISH);

  private final ReservationVM viewModel;

  public ReservationController(ReservationVM vm)
  {
    this.viewModel = vm;
  }

  public void initialize()
  {
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

        vehicleTable.setItems(viewModel.getFilteredVehicles());
      }
    });

    vehicleTable.setItems(viewModel.getVehicleList());
    vehicleTable.setEditable(false);
    vehicleTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    viewModel.selectedVehicleProperty()
        .bind(vehicleTable.getSelectionModel().selectedItemProperty());
    viewModel.selectedVehicleProperty().addListener((obs, oldValue, newV) -> {
      viewModel.setVisibility();
      if (newV != null)
      {
        viewModel.getIdProp().set(newV.idPropProperty().get());
        viewModel.conditionProperty().set(newV.conditionPropProperty().get());
        viewModel.colorProperty().set(newV.colorPropProperty().get());
        viewModel.getOwnerEmailProp().set(newV.ownerPropProperty().get());
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
    stateColumn.setCellValueFactory(new PropertyValueFactory<>("stateProp"));

    // Buttons
    backButton.setOnAction(e -> onBackButton());
    reserveButton.setOnAction(e -> onReserveButton());

    // Text fields
    conditionField.textProperty()
        .bindBidirectional(viewModel.conditionProperty());
    colorField.textProperty().bindBidirectional(viewModel.colorProperty());
    ownerField.textProperty().bindBidirectional(viewModel.getOwnerEmailProp());
    bikeTypeField.textProperty()
        .bindBidirectional(viewModel.bikeTypeProperty());
    speedField.textProperty().bindBidirectional(viewModel.speedProperty());
    rangeField.textProperty().bindBidirectional(viewModel.rangeProperty());
    priceField.textProperty().bindBidirectional(viewModel.finalPriceProperty());

    // Visibility
    conditionField.visibleProperty()
        .bind(viewModel.conditionFieldVisibilityProperty());
    conditionLabel.visibleProperty()
        .bind(viewModel.conditionLabelVisibilityProperty());
    colorField.visibleProperty().bind(viewModel.colorFieldVisibilityProperty());
    colorLabel.visibleProperty().bind(viewModel.colorLabelVisibilityProperty());
    ownerField.visibleProperty()
        .bind(viewModel.ownerEmailFieldVisibilityProperty());
    ownerLabel.visibleProperty()
        .bind(viewModel.ownerEmailLabelVisibilityProperty());
    speedLabel.visibleProperty().bind(viewModel.getSpeedLabelVisibility());
    speedField.visibleProperty().bind(viewModel.getSpeedFieldVisibility());
    rangeLabel.visibleProperty().bind(viewModel.getRangeLabelVisibility());
    rangeField.visibleProperty().bind(viewModel.getRangeFieldVisibility());
    bikeTypeLabel.visibleProperty()
        .bind(viewModel.getBikeTypeLabelVisibility());
    bikeTypeField.visibleProperty()
        .bind(viewModel.getBikeTypeFieldVisibility());
    datePicker.visibleProperty().bind(viewModel.datePickerVisibility());
    datePickerLabel.visibleProperty()
        .bind(viewModel.datePickerLabelVisibility());
    priceField.visibleProperty().bind(viewModel.priceFieldVisibility());
    priceLabel.visibleProperty().bind(viewModel.priceLabelVisibility());

    // Search Bar
    searchField.textProperty()
        .bindBidirectional(viewModel.searchQueryProperty());
    vehicleTable.setItems(viewModel.getVehicleList());

    // Date Picker
    datePicker.setValue(LocalDate.now());
    datePicker.setConverter(new StringConverter<LocalDate>()
    {
      @Override public String toString(LocalDate object)
      {
        if (iniDate != null && endDate != null)
        {
          return iniDate.format(formatter) + " - " + endDate.format(formatter);
        }
        return object.format(formatter);
      }

      @Override public LocalDate fromString(String string)
      {
        if (string.contains("-"))
        {
          try
          {
            iniDate = LocalDate.parse(string.split("-")[0].trim(), formatter);
            endDate = LocalDate.parse(string.split("-")[1].trim(), formatter);
          }
          catch (DateTimeParseException e)
          {
            return LocalDate.parse(string, formatter);
          }
          return iniDate;
        }
        return LocalDate.parse(string, formatter);
      }
    });

    datePicker.showingProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue)
      {
        // Get the DatePicker's Skin to access its content
        DatePickerSkin skin = (DatePickerSkin) datePicker.getSkin();
        Node content = skin.getPopupContent();

        Set<Node> rawCells = content.lookupAll(".day-cell");

        List<DateCell> cells = rawCells.stream()
            .filter(n -> n instanceof DateCell).map(n -> (DateCell) n)
            .filter(ce -> !ce.getStyleClass().contains("next-month"))
            .collect(Collectors.toList());

        // Select initial range
        if (iniDate != null && endDate != null)
        {
          int ini = iniDate.getDayOfMonth();
          int end = endDate.getDayOfMonth();
          cells.forEach(ce -> ce.getStyleClass().remove("selected"));
          cells.stream().filter(ce -> Integer.parseInt(ce.getText()) >= ini)
              .filter(ce -> Integer.parseInt(ce.getText()) <= end)
              .forEach(ce -> ce.getStyleClass().add("selected"));
        }

        iniCell = null;
        endCell = null;

        content.setOnMouseDragged(e -> {
          Node node = e.getPickResult().getIntersectedNode();
          DateCell cell = null;

          if (node instanceof DateCell)
          {
            cell = (DateCell) node;
          }
          else if (node instanceof Text)
          {
            cell = (DateCell) node.getParent();
          }

          if (cell != null && cell.getStyleClass().contains("day-cell")
              && !cell.getStyleClass().contains("next-month"))
          {
            if (iniCell == null)
            {
              iniCell = cell;
            }
            endCell = cell;
          }
          if (iniCell != null && endCell != null)
          {
            int ini = Math.min(Integer.parseInt(iniCell.getText()),
                Integer.parseInt(endCell.getText()));
            int end = Math.max(Integer.parseInt(iniCell.getText()),
                Integer.parseInt(endCell.getText()));
            cells.forEach(ce -> ce.getStyleClass().remove("selected"));
            cells.stream().filter(ce -> Integer.parseInt(ce.getText()) >= ini)
                .filter(ce -> Integer.parseInt(ce.getText()) <= end)
                .forEach(ce -> ce.getStyleClass().add("selected"));
          }
        });
        content.setOnMouseReleased(e -> {
          if (iniCell != null && endCell != null)
          {
            iniDate = LocalDate.of(datePicker.getValue().getYear(),
                datePicker.getValue().getMonth(),
                Integer.parseInt(iniCell.getText()));
            endDate = LocalDate.of(datePicker.getValue().getYear(),
                datePicker.getValue().getMonth(),
                Integer.parseInt(endCell.getText()));

            System.out.println("Selection from " + iniDate + " to " + endDate);

            datePicker.setValue(iniDate);
            int ini = iniDate.getDayOfMonth();
            int end = endDate.getDayOfMonth();
            cells.forEach(ce -> ce.getStyleClass().remove("selected"));
            cells.stream().filter(ce -> Integer.parseInt(ce.getText()) >= ini)
                .filter(ce -> Integer.parseInt(ce.getText()) <= end)
                .forEach(ce -> ce.getStyleClass().add("selected"));
          }
          endCell = null;
          iniCell = null;
          viewModel.startDateProperty().set(iniDate);
          viewModel.endDateProperty().set(endDate);
        });
      }
    });
  }

  public void onBackButton()
  {
    ViewHandler.showView(ViewType.WELCOME);
  }

  public void onReserveButton()
  {
    viewModel.addReservation();
  }

}
