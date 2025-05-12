package ui.studentaccount;

import javafx.beans.property.*;
import networking.studentaccount.StudentAccountClient;

public class StudentAccountVM
{
  private final StudentAccountClient studentAccountService;

  public StudentAccountVM(StudentAccountClient studentAccountService)
  {
    this.studentAccountService = studentAccountService;
  }

  // Fields
  private final StringProperty firstNameProp = new SimpleStringProperty();
  private final StringProperty lastNameProp = new SimpleStringProperty();
  private final StringProperty oldPasswordProp = new SimpleStringProperty();
  private final StringProperty newPasswordProp = new SimpleStringProperty();
  private final StringProperty confirmPasswordProp = new SimpleStringProperty();

  //Labels

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

  public void resetInfo()
  {
//    firstNameProp.set();
//    lastNameProp.set();
//    oldPasswordProp.set();
//    newPasswordProp.set();
//    confirmPasswordProp.set();
  }

  public void setVisibility()
  {
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

  public void onEditButton()
  {
    confirmPasswordLabelVisibility.set(true);
    confirmPasswordFieldVisibility.set(true);

    firstNameLabelVisibility.set(true);
    firstNameFieldVisibility.set(true);

    lastNameLabelVisibility.set(true);
    lastNameFieldVisibility.set(true);

    oldPasswordLabelVisibility.set(true);
    oldPasswordFieldVisibility.set(true);

    newPasswordLabelVisibility.set(true);
    newPasswordFieldVisibility.set(true);
  }
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
}
