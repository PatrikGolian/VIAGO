package ui.adminaccount;

import dtos.user.UserDataDto;
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
  private final BooleanProperty isAdmin = new SimpleBooleanProperty();
  private final StringProperty blackListReason = new SimpleStringProperty();

  public UserFx(UserDataDto user)
  {
    emailProp.set(user.email());
    firstNameProp.set(user.firstName());
    lastNameProp.set(user.lastName());
    isBlacklisted.set(user.isBlacklisted());
    isAdmin.set(user.isAdmin());
    blackListReason.set(user.blackListReason());
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

  public StringProperty blackListedReasonProperty()
  {
    return blackListReason;
  }
}
