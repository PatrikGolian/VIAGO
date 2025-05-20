package ui.register;

import dtos.auth.RegisterUserRequest;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import model.exceptions.ValidationException;
import networking.authentication.AuthenticationClient;
import utils.StringUtils;

import java.io.IOException;

public class RegisterVM
{
    private final StringProperty emailProp = new SimpleStringProperty();
    private final StringProperty passwordProp = new SimpleStringProperty();
    private final StringProperty repeatProp = new SimpleStringProperty();
    private final StringProperty firstNameProp = new SimpleStringProperty();
    private final StringProperty lastNameProp = new SimpleStringProperty();


    private final StringProperty messageProp = new SimpleStringProperty();
    private final BooleanProperty disableRegisterButtonProp = new SimpleBooleanProperty(true);
    private final AuthenticationClient authService;

    public RegisterVM(AuthenticationClient authService)
    {
        this.authService = authService;
        emailProp.addListener(this::updateRegisterButtonState);
        passwordProp.addListener(this::updateRegisterButtonState);
        repeatProp.addListener(this::updateRegisterButtonState);
        firstNameProp.addListener(this::updateRegisterButtonState);
        lastNameProp.addListener(this::updateRegisterButtonState);
    }

    public void registerUser()
    {
        // Clear potential existing message
        messageProp.set("");

        // Validate all input is present
        if (emailProp.get() == null || emailProp.get().isEmpty())
        {
            messageProp.set("Email cannot be empty");
            return;
        }
        else if(!(emailProp.get().matches(("^[a-zA-Z]+@via\\.dk$"))) && !(emailProp.get().matches("^\\d{6}@via\\.dk$")))
        {
            messageProp.set("Email format not recognized");
            return;
        }
        if (firstNameProp.get() == null || firstNameProp.get().isEmpty())
        {
            messageProp.set("First name cannot be empty");
            return;
        }
        if(firstNameProp.get().length() <= 2)
        {
            messageProp.set("First name has to have at least 3 letters");
            return;
        }
        if(!firstNameProp.get().matches("[a-zA-Z ]+"))
        {
            messageProp.set("First name can only contain letters");
            return;
        }
        if (lastNameProp.get() == null || lastNameProp.get().isEmpty())
        {
            messageProp.set("Last name cannot be empty");
            return;
        }
        if(lastNameProp.get().length() <= 2)
        {
            messageProp.set("Last name has to have at least 3 letters");
            return;
        }
        if(!lastNameProp.get().matches("[a-zA-Z ]+"))
        {
            messageProp.set("Last name can only contain letters");
            return;
        }
        if (passwordProp.get() == null || passwordProp.get().isEmpty())
        {
            messageProp.set("Password cannot be empty");
            return;
        }
        if (!passwordProp.get().equals(repeatProp.get()))
        {
            messageProp.set("Passwords do not match");
            System.out.println(passwordProp.get() + repeatProp.get());
            return;
        }
        if (passwordProp.get().length() < 8)
        {
            messageProp.set("Password must be 8 or more characters");
            return;
        }
        if (passwordProp.get().length() > 24)
        {
            messageProp.set("Password must be 24 or fewer characters");
            return;
        }
        try
        {
            authService.registerUser(new RegisterUserRequest(
                    emailProp.get(),
                    passwordProp.get(),
                    firstNameProp.get(),
                    lastNameProp.get())
            );
            System.out.println("No exception thrown - setting message to Success");
            messageProp.set("Success");
            clearFields();
        }
        catch (ValidationException ve) {
            messageProp.set(ve.getMessage());
            return;
        }
        catch (Exception e) {
            messageProp.set("Unexpected error: " + e.getMessage());
            return;
        }


    }

    private void clearFields()
    {
        emailProp.set("");
        passwordProp.set("");
        repeatProp.set("");
        firstNameProp.set("");
        lastNameProp.set("");
    }

    private void updateRegisterButtonState(Observable observable)
    {
        boolean shouldDisable =
                StringUtils.isNullOrEmpty(emailProp.get()) ||
                        StringUtils.isNullOrEmpty(passwordProp.get()) ||
                        StringUtils.isNullOrEmpty(repeatProp.get()) ||
                        StringUtils.isNullOrEmpty(firstNameProp.get()) ||
                        StringUtils.isNullOrEmpty(lastNameProp.get());

        disableRegisterButtonProp.set(shouldDisable);
    }

    public StringProperty emailProperty()
    {
        return emailProp;
    }

    public StringProperty passwordProperty()
    {
        return passwordProp;
    }

    public StringProperty repeatProperty()
    {
        return repeatProp;
    }

    public StringProperty messageProperty()
    {
        return messageProp;
    }

    public BooleanProperty disableRegisterButtonProperty()
    {
        return disableRegisterButtonProp;
    }

    public StringProperty firstNameProperty()
    {
        return firstNameProp;
    }

    public StringProperty lastNameProperty()
    {
        return lastNameProp;
    }
}
