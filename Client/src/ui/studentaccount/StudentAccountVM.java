package ui.studentaccount;

import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.UserDataDto;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.studentaccount.StudentAccountClient;
import startup.ViewHandler;
import state.AppState;
import ui.popup.MessageType;
import ui.reservation.ReservationFx;

import java.time.LocalDate;
import java.util.List;

public class StudentAccountVM
{
  private final StudentAccountClient studentAccountService;
  private final ObjectProperty<ReservationFx> selectedReservation = new SimpleObjectProperty<>();
  private final ObservableList<ReservationFx> reservations = FXCollections.observableArrayList();

  // Editable Fields
  private final StringProperty profileTextRedirectProperty = new SimpleStringProperty();
  private final StringProperty coverLabelProperty = new SimpleStringProperty();
  private final StringProperty firstNameProp = new SimpleStringProperty();
  private final StringProperty lastNameProp = new SimpleStringProperty();
  private final StringProperty oldPasswordProp = new SimpleStringProperty();
  private final StringProperty newPasswordProp = new SimpleStringProperty();
  private final StringProperty confirmPasswordProp = new SimpleStringProperty();
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty nameProp = new SimpleStringProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty messageProp2 = new SimpleStringProperty();

  // Visiblity bind
  private final BooleanProperty changeNameLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty changePasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty firstNameFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty lastNameFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty oldPasswordFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty newPasswordFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty confirmPasswordFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty confirmButtonVisibility = new SimpleBooleanProperty();

  private final BooleanProperty firstNameLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty lastNameLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty oldPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty newPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty confirmPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty messageLabelVisibility = new SimpleBooleanProperty();

  private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
  private ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();


  public StudentAccountVM(StudentAccountClient studentAccountService)
  {
    this.studentAccountService = studentAccountService;
    loadReservations();
  }



  public void resetInfo()
  {
    AppState.setCurrentUser(AppState.getCurrentUser());

    String firstName = AppState.getCurrentUser().firstName();
    String lastName = AppState.getCurrentUser().lastName();

    nameProp.set(firstName+" "+lastName);
    emailProp.set(AppState.getCurrentUser().email());
    firstNameProp.set(firstName);
    lastNameProp.set(lastName);
    oldPasswordProp.set("");
    newPasswordProp.set("");
    confirmPasswordProp.set("");

  }

  public void loadReservations()
  {
      try
      {
        emailProp.set(AppState.getCurrentUser().email());
        List<ReservationDto> loadedReservations = studentAccountService.getReservations(new ReservationReserveRequest(emailProp.get()));
        for (ReservationDto reservation : loadedReservations)
        {
          reservations.add(new ReservationFx(reservation));
        }
      }
      catch (Exception e)
      {
        ViewHandler.popupMessage(MessageType.ERROR, e.getMessage());
      }
  }
  public void deleteReservation(ReservationFx reservationFx)
  {
    if (reservationFx == null) {
      return;
    }
    try
    {
      int id = reservationFx.idPropProperty().get();
      String type = reservationFx.typePropProperty().get();
      String startDateStr = reservationFx.startDatePropProperty().get();
      String endDateStr = reservationFx.endDatePropProperty().get();
      double price = reservationFx.pricePropProperty().get();
      String ownerEmail = reservationFx.ownerEmailPropProperty().get();
      String reservedByEmail = reservationFx.getReservedByProp().get();

      // Parse start date
      String[] startParts = startDateStr.split("/");
      int startDay = Integer.parseInt(startParts[0]);
      int startMonth = Integer.parseInt(startParts[1]);
      int startYear = Integer.parseInt(startParts[2]);
      model.Date start = new model.Date(startDay, startMonth, startYear);

      // Parse end date
      String[] endParts = endDateStr.split("/");
      int endDay = Integer.parseInt(endParts[0]);
      int endMonth = Integer.parseInt(endParts[1]);
      int endYear = Integer.parseInt(endParts[2]);
      model.Date end = new model.Date(endDay, endMonth, endYear);


      LocalDate today = LocalDate.now();
      LocalDate startLocal = LocalDate.of(startYear, startMonth, startDay);

      if (!today.isBefore(startLocal)) {
        messageProp.set("You cannot delete past or current reservations.");
        return;
      }

      ReservationRequest request = new ReservationRequest(
          id,
          type,
          ownerEmail,
          reservedByEmail,
          start,
          end,
          price
      );


      studentAccountService.delete(request);
      messageProp.set("Success");
    }
    catch (Exception e)
    {
      messageProp.set("An error occurred while trying to delete the reservation.");
      e.printStackTrace();
    }
  }


  public ObservableList<ReservationFx> getReservationList()
  {
    return reservations;
  }
  public void setVisibility()
  {
    messageLabelVisibility.set(false);
    confirmPasswordLabelVisibility.set(false);
    confirmPasswordFieldVisibility.set(false);

    firstNameLabelVisibility.set(false);
    firstNameFieldVisibility.set(false);

    lastNameLabelVisibility.set(false);
    lastNameFieldVisibility.set(false);

    oldPasswordLabelVisibility.set(false);
    oldPasswordFieldVisibility.set(false);

    newPasswordLabelVisibility.set(false);
    newPasswordFieldVisibility.set(false);

    changeNameLabelVisibility.set(false);
    changePasswordLabelVisibility.set(false);
    confirmButtonVisibility.set(false);
  }

  public void toggleEditMode()
  {
    boolean currentlyVisible = firstNameFieldVisibility.get(); // Use any field's visibility
    boolean newVisibility = !currentlyVisible;

    confirmPasswordLabelVisibility.set(newVisibility);
    confirmPasswordFieldVisibility.set(newVisibility);

    firstNameLabelVisibility.set(newVisibility);
    firstNameFieldVisibility.set(newVisibility);

    lastNameLabelVisibility.set(newVisibility);
    lastNameFieldVisibility.set(newVisibility);

    oldPasswordLabelVisibility.set(newVisibility);
    oldPasswordFieldVisibility.set(newVisibility);

    newPasswordLabelVisibility.set(newVisibility);
    newPasswordFieldVisibility.set(newVisibility);

    changePasswordLabelVisibility.set(newVisibility);
    changeNameLabelVisibility.set(newVisibility);

    messageLabelVisibility.set(newVisibility);
    confirmButtonVisibility.set(newVisibility);
  }

  public void confirmEdit()
  {
    validateInfo();
    resetInfo();
  }

  private void validateInfo() {
    String first = firstNameProp.get().trim();
    String last  = lastNameProp.get().trim();

    // --- 1) Name validation (always) ---
    if (first.isEmpty()) {
      messageProp.set("First name cannot be empty");
      return;
    }
    if (first.length() < 2 || !first.matches("[a-zA-Z ]+")) {
      messageProp.set("First name must be at least 2 letters and only letters");
      return;
    }
    if (last.isEmpty()) {
      messageProp.set("Last name cannot be empty");
      return;
    }
    if (last.length() < 2 || !last.matches("[a-zA-Z ]+")) {
      messageProp.set("Last name must be at least 2 letters and only letters");
      return;
    }

    // --- 2) Password branch (only if they filled in “newPassword”) ---
    String finalPassword;
    boolean wantsPasswordChange =
        newPasswordProp.get() != null && !newPasswordProp.get().isBlank();

    if (wantsPasswordChange) {
      String oldPwd     = oldPasswordProp.get();
      String newPwd     = newPasswordProp.get();
      String confirmPwd = confirmPasswordProp.get();
      // must supply old
      if (oldPwd == null || oldPwd.isBlank()) {
        messageProp.set("Please enter your old password to change it");
        return;
      }
      // verify old matches what’s on the server
      String oldPwds           = oldPasswordProp.get();
      String currentOnServerU  = getOldPassword(new GetPasswordRequest(emailProp.get()));

      System.out.println("⏱ Typed oldPwd    = [" + oldPwds + "]");
      System.out.println("⏱ Server password= [" + currentOnServerU + "]");
      String currentOnServer = getOldPassword(new GetPasswordRequest(emailProp.get()));
      if (!oldPwd.equals(currentOnServer)) {
        messageProp.set("Old password is not correct");
        return;
      }
      // new/confirm must match
      if (!newPwd.equals(confirmPwd)) {
        messageProp.set("New passwords do not match");
        return;
      }
      finalPassword = newPwd;
    } else {
      // no password change requested → keep the existing
      finalPassword = getOldPassword(new GetPasswordRequest(emailProp.get()));
    }

    // --- 3) Send a single ChangeUserRequest with the name + finalPassword ---
    try {
      ChangeUserRequest req = new ChangeUserRequest(
          first, last, emailProp.get(), finalPassword
      );
      changeUser(req);
      ChangeUserRequest request = new ChangeUserRequest(first, last, emailProp.get(), finalPassword);
      changeUser(request);

      var oldDto = AppState.getCurrentUser();
      var updatedDto = new UserDataDto(
          oldDto.email(),
          first,
          last, oldDto.isBlacklisted(),
          oldDto.isAdmin(), oldDto.blackListReason()
      );
      AppState.setCurrentUser(updatedDto);

      resetInfo();
      messageProp.set("Success!");
    }
    catch (Exception ex) {
      messageProp.set("Update failed: " + ex.getMessage());
    }
  }

  private String getOldPassword(GetPasswordRequest request)
  {
    return studentAccountService.getPassword(request);
  }

  private void changeUser(ChangeUserRequest request)
  {
    studentAccountService.changeUser(request);
  }

  // Editable fields
  public Property<String> firstNameProperty()
  {
    return firstNameProp;
  }

  public Property<String> lastNameProperty()
  {
    return lastNameProp;
  }

  public Property<String> oldPasswordProperty()
  {
    return oldPasswordProp;
  }

  public Property<String> newPasswordProperty()
  {
    return newPasswordProp;
  }

  public Property<String> confirmPasswordProperty()
  {
    return confirmPasswordProp;
  }

  public Property<String> messageLabelProperty()
  {
    return messageProp;
  }


  // Visibilty of editable fields
  public BooleanProperty firstNameFieldVisibilityProperty()
  {
    return firstNameFieldVisibility;
  }

  public BooleanProperty firstNameLabelVisibilityProperty()
  {
    return firstNameLabelVisibility;
  }

  public BooleanProperty lastNameFieldVisibilityProperty()
  {
    return lastNameFieldVisibility;
  }

  public BooleanProperty lastNameLabelVisibilityProperty()
  {
    return lastNameLabelVisibility;
  }

  public BooleanProperty oldPasswordFieldVisibilityProperty()
  {
    return oldPasswordFieldVisibility;
  }

  public BooleanProperty oldPasswordLabelVisibilityProperty()
  {
    return oldPasswordLabelVisibility;
  }

  public BooleanProperty newPasswordFieldVisibilityProperty()
  {
    return newPasswordFieldVisibility;
  }

  public BooleanProperty newPasswordLabelVisibilityProperty()
  {
    return newPasswordLabelVisibility;
  }

  public BooleanProperty confirmPasswordFieldVisibilityProperty()
  {
    return confirmPasswordFieldVisibility;
  }

  public BooleanProperty confirmPasswordLabelVisibilityProperty()
  {
    return confirmPasswordLabelVisibility;
  }

  public BooleanProperty messageLabelVisibilityProperty()
  {
    return messageLabelVisibility;
  }

  public Property<String> emailPropery()
  {
    return emailProp;
  }

  public Property<String> nameProperty()
  {
    return nameProp;
  }


  public BooleanProperty changeNameLabelVisibility()
  {
    return  changeNameLabelVisibility;
  }

  public BooleanProperty changePasswordLabelVisibility()
  {
    return changePasswordLabelVisibility;
  }

  public BooleanProperty confirmButtonVisibility()
  {
    return confirmButtonVisibility;
  }

  public StringProperty profileTextRedirectProperty()
  {
    return profileTextRedirectProperty;
  }

  public  StringProperty coverLabelProperty()
  {
    return coverLabelProperty;
  }

  public void setProfileInitials()
  {
    String firstname = AppState.getCurrentUser().firstName();
    String lastname = AppState.getCurrentUser().lastName();
    profileTextRedirectProperty.set("" + firstname.charAt(0) + lastname.charAt(0));
    coverLabelProperty.set("" + firstname.charAt(0) + lastname.charAt(0));
  }

}
