package ui.welcome;

import startup.ViewHandler;
import startup.ViewType;
import ui.common.Controller;

public class WelcomeController implements Controller
{
    public void openLoginView()
    {
        ViewHandler.showView(ViewType.LOGIN);
    }

    public void openRegisterView()
    {
        ViewHandler.showView(ViewType.REGISTER);
    }
}
