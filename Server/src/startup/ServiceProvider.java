package startup;

import model.exceptions.NoSuchServiceException;
import networking.requesthandlers.AuthRequestHandler;
import networking.requesthandlers.RequestHandler;
import networking.requesthandlers.UserRequestHandler;
import persistence.daos.user.UserDao;
import persistence.daos.user.UserJsonFileDao;
import persistence.daos.user.UserListDao;
import services.authentication.AuthServiceImpl;
import services.authentication.AuthenticationService;
import services.user.UserService;
import services.user.UserServiceImpl;
import utilities.logging.ConsoleLogger;
import utilities.logging.LogLevel;
import utilities.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ServiceProvider
{

    // The service provider is responsible for providing services, where needed.
    // It ensures various services receives the correct arguments.
    // If an implementation needs to be swapped out, e.g. DAO implementations,
    // we can do that a single place, and it will take effect across the entire application.

    // This is a fairly crude and basic implementation of the Service Locator pattern.

    public RequestHandler getAuthenticationRequestHandler()
    {
        return new AuthRequestHandler(getAuthenticationService());
    }

    public RequestHandler getUserRequestHandler()
    {
        return new UserRequestHandler(getUserService());
    }

    public Logger getLogger()
    {
        return new ConsoleLogger(LogLevel.INFO);
    }

    private static AuthenticationService getAuthenticationService()
    {
        return new AuthServiceImpl(getUserDao());
    }

    private static UserService getUserService()
    {
        return new UserServiceImpl(getUserDao());
    }

    private static UserDao getUserDao()
    {
        return new UserJsonFileDao();
    }




















    // Ignore the below code!!
    // ------------------------------
    // I am not currently using this. Just checking out what Java can do with generics.
    public <T> T getService(Class<T> serviceType)
    {
        if (!serviceRegistry.containsKey(serviceType))
        {

            throw new NoSuchServiceException(serviceType.getName());
        }
        return (T) serviceRegistry.get(serviceType);
    }

    private static Map<Class<?>, Supplier<?>> serviceRegistry = new HashMap<>();

    static
    {
        serviceRegistry.put(
                AuthRequestHandler.class,
                () -> new AuthRequestHandler((AuthenticationService) serviceRegistry.get(AuthenticationService.class).get())
        );
        serviceRegistry.put(
                AuthenticationService.class,
                () -> new AuthServiceImpl((UserDao) serviceRegistry.get(UserDao.class).get())
        );
        serviceRegistry.put(
                UserDao.class,
                () -> new UserListDao()
        );

    }
}
