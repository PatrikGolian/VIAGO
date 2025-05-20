package ui.reservation;

import dtos.reservation.ReservationRequestByIdType;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import model.entities.reservation.Reservation;
import services.reservation.ReservationService;
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
  @FXML private Label dkkShower;
  @FXML private Rectangle profileShapeRedirect;
  @FXML private Label profileTextRedirect;
  @FXML private Label rentRedirect;
  @FXML private Label myVehiclesRedirect;
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
  @FXML private Label priceField;
  @FXML private TextField searchField;
  @FXML private Label bikeTypeLabel;
  @FXML private Label speedLabel;
  @FXML private Label rangeLabel;
  @FXML private Button reserveButton;
  @FXML private Label conditionLabel;
  @FXML private Label colorLabel;
  @FXML private Label ownerLabel;
  @FXML private Label priceLabel;
  @FXML private Label datePickerLabel;
  @FXML private Label messageLabel;

  @FXML private DatePicker datePicker;
  ReservationService service;
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
    viewModel.loadVehicles();
    // Text fields uneditable and unfocusable
    conditionField.setEditable(false);
    conditionField.setFocusTraversable(false);

    colorField.setEditable(false);
    colorField.setFocusTraversable(false);

    ownerField.setEditable(false);
    ownerField.setFocusTraversable(false);

    bikeTypeField.setEditable(false);
    bikeTypeField.setFocusTraversable(false);

    speedField.setEditable(false);
    speedField.setFocusTraversable(false);

    rangeField.setEditable(false);
    rangeField.setFocusTraversable(false);

    profileTextRedirect.textProperty().bindBidirectional(
        viewModel.profileTextRedirectProperty());
    viewModel.setProfileInitials();

    dkkShower.visibleProperty().bind(viewModel.dkkShowerVisibility());

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

        vehicleTable.setItems(viewModel.getFilteredVehicles());
      }
    });

    // Table view
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
    messageLabel.textProperty().bind(viewModel.messageProperty());

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

    viewModel.reservationSuccessProperty()
        .addListener((obs, oldVal, newVal) -> {
          if (newVal)
          {
            viewModel.update();
            viewModel.loadVehicles();
            vehicleTable.refresh();

            // Reset the flag to avoid repeated triggers
            viewModel.reservationSuccessProperty().set(false);
          }
        });

    datePicker.valueProperty().bindBidirectional(viewModel.datePickerProp());

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
        ReservationRequestByIdType request = new ReservationRequestByIdType(
            viewModel.selectedVehicleProperty().get().idPropProperty().get(),
            viewModel.selectedVehicleProperty().get().typePropProperty().get());
        List<Reservation> reservations = viewModel.getReservationsByTypeAndId(
            request);
        datePicker.setDayCellFactory(dp -> new DateCell() {
          @Override public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);

            getStyleClass().removeAll("reserved-day", "past-day");
            setDisable(false);

            if (empty || item == null) {
              setText(null);
              return;
            }

            if (item.isBefore(LocalDate.now())) {
              setDisable(true);
              getStyleClass().add("past-day");
              return;
            }

            boolean isReserved = reservations.stream().anyMatch(res -> {
              LocalDate start = LocalDate.of(
                  res.getStartDate().getYear(),
                  res.getStartDate().getMonth(),
                  res.getStartDate().getDay());
              LocalDate end = LocalDate.of(
                  res.getEndDate().getYear(),
                  res.getEndDate().getMonth(),
                  res.getEndDate().getDay());
              return    item.equals(start)
                  || item.equals(end)
                  || (item.isAfter(start) && item.isBefore(end));
            });

            if (isReserved) {
              setDisable(true);
              getStyleClass().add("reserved-day");
            }
          }
        });

        /*datePicker.setDayCellFactory(dp -> new DateCell()
        {
          @Override public void updateItem(LocalDate item, boolean empty)
          {
            if (!(empty || item == null))
            {
              if (reservations != null)
              {
                boolean met = false;
                for (Reservation res : reservations)
                {
                  Date s = res.getStartDate();
                  Date e = res.getEndDate();
                  LocalDate starts = LocalDate.of(s.getYear(), s.getMonth(),
                      s.getDay());
                  LocalDate ends = LocalDate.of(e.getYear(), e.getMonth(),
                      e.getDay());

                  if ((item.isEqual(starts) || item.isEqual(ends) || (
                      item.isAfter(starts) && item.isBefore(ends))))
                  {
                    //System.out.println(starts);
                    met = true;
                    break;
                  }
                }

                if (!met)
                {
                  //System.out.println("set disable false");
                  setDisable(false);
                }
                else
                {
                  setDisable(true);
                  //System.out.println("set for "+item);
                  //System.out.println("empty: "+empty);
                  setStyle("-fx-background-color: #ffc0cb;");
                }
              }
              if (item.isBefore(LocalDate.now()))
              {
                setDisable(true);
                setStyle("-fx-background-color: #D3D3D3;");
              }
            }
            super.updateItem(item, empty);
          }
        });*/

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
          if (iniDate != null && endDate != null)
          {
            viewModel.startDateProperty().set(iniDate);
            viewModel.endDateProperty().set(endDate);
            viewModel.setFinalPrice();
          }
        });
      }
    });
  }

  public void onReserveButton()
  {
    if (viewModel.selectedVehicleProperty().get() == null || iniDate == null
        || endDate == null)
    {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Reservation Error");
      alert.setHeaderText(null);
      alert.setContentText("Select vehicle and choose rental period.");
      alert.showAndWait();
      return;
    }
    viewModel.addReservation();
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

