package ui.studentaccount;

import dtos.auth.RegisterUserRequest;
import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.ViewUsers;
import dtos.vehicle.VehicleDisplayDto;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.authentication.AuthenticationClient;
import networking.authentication.SocketAuthenticationClient;
import networking.studentaccount.StudentAccountClient;
import startup.ViewHandler;
import state.AppState;
import ui.popup.MessageType;
import ui.register.RegisterVM;
import ui.reservation.ReservationFx;
import ui.reservation.VehicleFx;
import ui.viewusers.UserFx;

import java.util.List;

public class StudentAccountVM
{
  private final StudentAccountClient studentAccountService;
  private final ObservableList<ReservationFx> reservations = FXCollections.observableArrayList();

  public StudentAccountVM(StudentAccountClient studentAccountService)
  {
    this.studentAccountService = studentAccountService;
    loadReservations();
  }

  // Editable Fields
  private final StringProperty firstNameProp = new SimpleStringProperty();
  private final StringProperty lastNameProp = new SimpleStringProperty();
  private final StringProperty oldPasswordProp = new SimpleStringProperty();
  private final StringProperty newPasswordProp = new SimpleStringProperty();
  private final StringProperty confirmPasswordProp = new SimpleStringProperty();
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty nameProp = new SimpleStringProperty();
  private final StringProperty messageProp = new SimpleStringProperty();

  // Visiblity bind
  private final BooleanProperty firstNameFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty lastNameFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty oldPasswordFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty newPasswordFieldVisibility = new SimpleBooleanProperty();
  private final BooleanProperty confirmPasswordFieldVisibility = new SimpleBooleanProperty();

  private final BooleanProperty firstNameLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty lastNameLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty oldPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty newPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty confirmPasswordLabelVisibility = new SimpleBooleanProperty();
  private final BooleanProperty messageLabelVisibility = new SimpleBooleanProperty();

  public void resetInfo()
  {
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
    messageLabelVisibility.set(newVisibility);
  }

  public void ConfirmEdit()
  {
    validateInfo();
    resetInfo();
  }

  private void validateInfo()
  {
    if (oldPasswordProp.get().isEmpty()&&newPasswordProp.get().isEmpty()&&confirmPasswordProp.get().isEmpty())
    {
      try
      {
        String password = getOldPassword(new GetPasswordRequest(emailProp.get()));
        ChangeUserRequest request = new ChangeUserRequest(
            firstNameProp.get(), lastNameProp.get(), emailProp.get(), password
        );
        changeUser(request);
        resetInfo();
        messageProp.set("Success");
      }
      catch (Exception e)
      {
        // might receive exception from lower layer (i.e. client)
        messageProp.set(e.getMessage());
      }
    }
    else
    {
      if (oldPasswordProp.get()
          .equals(getOldPassword(new GetPasswordRequest(emailProp.get()))))
      {
        messageProp.set("Old password is not coorect!");
        return;
      }
      if (firstNameProp.get() == null || firstNameProp.get().isEmpty())
      {
        messageProp.set("First name cannot be empty");
        return;
      }
      if (firstNameProp.get().length() < 2)
      {
        messageProp.set("First name has to have at least 3 letters");
        return;
      }
      if (!firstNameProp.get().matches("[a-zA-Z ]+"))
      {
        messageProp.set("First name can only contain letters");
        return;
      }
      if (lastNameProp.get() == null || lastNameProp.get().isEmpty())
      {
        messageProp.set("Last name cannot be empty");
        return;
      }
      if (lastNameProp.get().length() < 2)
      {
        messageProp.set("Last name has to have at least 3 letters");
        return;
      }
      if (!lastNameProp.get().matches("[a-zA-Z ]+"))
      {
        messageProp.set("Last name can only contain letters");
        return;
      }
      if (newPasswordProp.get() == null || newPasswordProp.get().isEmpty())
      {
        messageProp.set("Password cannot be empty");
        return;
      }
      if (!newPasswordProp.get().equals(confirmPasswordProp.get()))
      {
        messageProp.set("Passwords do not match");
        System.out.println(newPasswordProp.get() + confirmPasswordProp.get());
        return;
      }
      try
      {
        ChangeUserRequest request = new ChangeUserRequest(firstNameProp.get(),
            lastNameProp.get(), emailProp.get(), newPasswordProp.get());
        changeUser(request);
        resetInfo();
        messageProp.set("Success");
      }
      catch (Exception e)
      {
        // might receive exception from lower layer (i.e. client)
        messageProp.set(e.getMessage());
      }
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
}
