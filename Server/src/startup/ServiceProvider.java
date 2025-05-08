package startup;

import model.exceptions.NoSuchServiceException;
import networking.requesthandlers.*;
import persistence.daos.reservation.ReservationDao;
import persistence.daos.reservation.ReservationPostgresDao;
import persistence.daos.user.UserDao;
import persistence.daos.user.UserJsonFileDao;
import persistence.daos.user.UserListDao;
import persistence.daos.user.UserPostgresDao;
import services.authentication.AuthServiceImpl;
import services.authentication.AuthenticationService;
import services.reservation.ReservationService;
import services.reservation.ReservationServiceImpl;
import services.user.UserService;
import services.user.UserServiceImpl;
import services.vehicle.VehicleService;
import services.vehicle.VehicleServiceImpl;
import utilities.logging.ConsoleLogger;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import persistence.daos.vehicle.VehicleDao;
import persistence.daos.vehicle.VehiclePostgresDao;

import java.sql.SQLException;
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

    public RequestHandler getAddNewVehicleRequestHandler()
    {
        return new AddNewVehicleRequestHandler(getVehicleService());
    }

    public RequestHandler getReservationRequestHandler()
    {
        return new ReservationRequestHandler(getReservationService(),
            getVehicleService());
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

    private static VehicleService getVehicleService()
    {
        return new VehicleServiceImpl(getVehicleDao());
    }

    private static UserDao getUserDao()
    {
        try
        {
            return UserPostgresDao.getInstance();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to initialize DAO", e); // or handle as needed
        }
    }

    private static VehicleDao getVehicleDao()
    {
        try
        {
            return VehiclePostgresDao.getInstance();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to initialize DAO", e); // or handle as needed
        }
    }

    private static ReservationDao getReservationDao()
    {
        try
        {
            return ReservationPostgresDao.getInstance();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to initialize DAO", e); // or handle as needed
        }
    }

    static ReservationService getReservationService()
    {
        return new ReservationServiceImpl(getReservationDao(), getVehicleDao());
    }

}










//
//    // Ignore the below code!!
//    // ------------------------------
//    // I am not currently using this. Just checking out what Java can do with generics.
//    public <T> T getService(Class<T> serviceType)
//    {
//        if (!serviceRegistry.containsKey(serviceType))
//        {
//
//            throw new NoSuchServiceException(serviceType.getName());
//        }
//        return (T) serviceRegistry.get(serviceType);
//    }
//
//    private static Map<Class<?>, Supplier<?>> serviceRegistry = new HashMap<>();
//
//    static
//    {
//        serviceRegistry.put(
//                AuthRequestHandler.class,
//                () -> new AuthRequestHandler((AuthenticationService) serviceRegistry.get(AuthenticationService.class).get())
//        );
//        serviceRegistry.put(
//                AuthenticationService.class,
//                () -> new AuthServiceImpl((UserDao) serviceRegistry.get(UserDao.class).get())
//        );
//        serviceRegistry.put(
//                UserDao.class,
//                () -> new UserListDao()
//        );
//
//    }
//}
