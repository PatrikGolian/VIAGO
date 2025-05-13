package ui.adminaccount;

import dtos.user.ViewUsers;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserFx
{
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty firstNameProp = new SimpleStringProperty();
  private final StringProperty lastNameProp = new SimpleStringProperty();
  private final BooleanProperty isBlacklisted = new SimpleBooleanProperty();

  public UserFx(ViewUsers.UserDisplayDto user)
  {
    emailProp.set(user.email());
    firstNameProp.set(user.firstName());
    lastNameProp.set(user.lastName());
    isBlacklisted.set(user.isBlacklisted());
  }

  public StringProperty emailProperty()
  {
    return emailProp;
  }

  public StringProperty firstNameProperty()
  {
    return firstNameProp;
  }

  public StringProperty lastNameProperty()
  {
    return lastNameProp;
  }

  public BooleanProperty isBlacklistedProperty()
  {
    return isBlacklisted;
  }
}
