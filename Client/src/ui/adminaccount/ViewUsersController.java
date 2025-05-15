package ui.adminaccount;

import javafx.beans.property.Property;
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
import ui.studentaccount.StudentAccountVM;

import java.sql.SQLException;

public class ViewUsersController implements Controller
{
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
  @FXML TextField blackListReasonField;
  @FXML Button confirmButton;
  @FXML Button editButton;
  @FXML Label firstLabel;
  @FXML Label lastLabel;
  @FXML Label oldLabel;
  @FXML Label newLabel;
  @FXML Label confirmLabel;
  @FXML Label messageLabel;
  @FXML Label nameLabel;
  @FXML Label emailLabel;
  @FXML Label allVehiclesRedirect;
  @FXML private Label blackListReasonLabel;

  @FXML Button blacklistButton;
  @FXML TableView<UserFx> table;
  @FXML TableColumn<UserFx, String> firstNameColumn;
  @FXML TableColumn<UserFx, String> lastNameColumn;
  @FXML TableColumn<UserFx, String> emailColumn;
  @FXML TableColumn<UserFx, Boolean> blacklistedColumn;

  private final ViewUsersVM vm;

  public ViewUsersController(ViewUsersVM vm)
  {
    this.vm = vm;
  }

  public void initialize()
  {
    // Bind edit fields and labels
    firstNameField.textProperty().bindBidirectional(vm.firstNameProperty());
    lastNameField.textProperty().bindBidirectional(vm.lastNameProperty());
    oldPasswordField.textProperty().bindBidirectional(vm.oldPasswordProperty());
    newPasswordField.textProperty().bindBidirectional(vm.newPasswordProperty());
    confirmPasswordField.textProperty()
        .bindBidirectional(vm.confirmPasswordProperty());

    // Bind welcome labels
    emailLabel.textProperty().bind(vm.emailPropery());
    nameLabel.textProperty().bind(vm.nameProperty());

    // Bind succes message
    messageLabel.textProperty().bind(vm.messageLabelProperty());

    // Bind visibilty of edit fields and labels
    firstNameField.visibleProperty()
        .bind(vm.firstNameFieldVisibilityProperty());
    firstLabel.visibleProperty().bind(vm.firstNameLabelVisibilityProperty());
    lastNameField.visibleProperty().bind(vm.lastNameFieldVisibilityProperty());
    lastLabel.visibleProperty().bind(vm.lastNameLabelVisibilityProperty());
    oldPasswordField.visibleProperty()
        .bind(vm.oldPasswordFieldVisibilityProperty());
    oldLabel.visibleProperty().bind(vm.oldPasswordLabelVisibilityProperty());
    newPasswordField.visibleProperty()
        .bind(vm.newPasswordFieldVisibilityProperty());
    newLabel.visibleProperty().bind(vm.newPasswordLabelVisibilityProperty());
    confirmPasswordField.visibleProperty()
        .bind(vm.confirmPasswordFieldVisibilityProperty());
    confirmLabel.visibleProperty()
        .bind(vm.confirmPasswordLabelVisibilityProperty());
    confirmButton.visibleProperty().bind(vm.confirmButtonVisibility());

    blacklistButton.textProperty().bind(vm.blackListButtonNameProperty());

    changeNameLabel.visibleProperty().bind(vm.changeNameLabelVisibility());
    changePasswordLabel.visibleProperty()
        .bind(vm.changePasswordLabelVisibility());


    profileTextRedirect.textProperty()
        .bindBidirectional(vm.profileTextRedirectProperty());
    coverLabel.textProperty().bindBidirectional(vm.coverLabelProperty());
    vm.setProfileInitials();

    blackListReasonField.textProperty().bindBidirectional(vm.blackListedReasonFieldProperty());
    blackListReasonLabel.visibleProperty().bind(vm.getBlackListLabelVisibility());
    blackListReasonField.visibleProperty().bind(vm.getBlackListFieldVisibility());

    // Sets editable fields' visibility
    vm.setVisibilityEditFields();
    vm.setVisibility();

    setFieldsAndLabels();

    vm.loadUsers();

    // Table view
    table.setItems(vm.getUsersList());
    table.setEditable(false);
    table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    blackListReasonField.setEditable(false);
    blackListReasonField.setFocusTraversable(false);


    vm.selectedUserProperty().bind(table.getSelectionModel().selectedItemProperty());
    vm.selectedUserProperty().addListener((obs, oldValue, newV) -> {
          if (newV != null)
          {
            vm.blackListButtonName(newV.isBlacklistedProperty().get());
            vm.blackListFieldSet(vm.getBlacklistReason(newV));
            vm.setVisibility();
            select(newV);
          }
        });

    firstNameColumn.setCellValueFactory(
        param -> param.getValue().firstNameProperty());
    lastNameColumn.setCellValueFactory(
        param -> param.getValue().lastNameProperty());
    emailColumn.setCellValueFactory(param -> param.getValue().emailProperty());
    blacklistedColumn.setCellValueFactory(
        param -> param.getValue().isBlacklistedProperty());


  }

  private void setFieldsAndLabels()
  {
    vm.resetInfo();
  }

  public void onEditButton()
  {
    vm.toggleEditMode();
  }

  public void onConfirmButton() throws SQLException
  {
    vm.confirmEdit();
  }

  public void onAllVehiclesRedirect()
  {
    ViewHandler.showView(ViewType.ADMINALLVEHICLES);
  }

  public void onProfileRedirect()
  {
    ViewHandler.showView(ViewType.VIEWUSERS);
  }

    public void onBlacklist()
    {
      UserFx selected = table.getSelectionModel().getSelectedItem();
      if (selected != null)
      {
        vm.blacklist(selected);
        clearTable();
        vm.loadUsers();
      }
      else
      {
        ViewHandler.popupMessage(MessageType.WARNING, "Please select a user to blacklist.");
      }
    }
  private void updateBlackListedReasonField()
  {
    UserFx selected = table.getSelectionModel().getSelectedItem();
    String blackListedReason = selected.blackListedReasonProperty().get();
  }
  public void clearTable()
  {
    table.getItems().clear();
  }

  public void onLogout()
  {
    ViewHandler.showView(ViewType.LOGIN);
  }

  public void select(UserFx selected)
  {
    vm.setSelected(selected);
  }

}











