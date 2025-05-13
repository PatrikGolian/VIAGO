package startup;

import networking.requesthandlers.*;
import persistence.reservation.ReservationDao;
import persistence.reservation.ReservationPostgresDao;
import persistence.user.UserDao;
import persistence.user.UserPostgresDao;
import services.authentication.AuthServiceImpl;
import services.authentication.AuthenticationService;
import services.myvehicles.MyVehiclesService;
import services.myvehicles.MyVehiclesServiceImpl;
import services.reservation.ReservationService;
import services.reservation.ReservationServiceImpl;
import services.studentaccount.StudentAccountService;
import services.studentaccount.StudentAccountServiceImpl;
import services.user.UserService;
import services.user.UserServiceImpl;
import services.vehicle.VehicleService;
import services.vehicle.VehicleServiceImpl;
import utilities.logging.ConsoleLogger;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import persistence.vehicle.VehicleDao;
import persistence.vehicle.VehiclePostgresDao;

import java.sql.SQLException;

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

  public RequestHandler getStudentAccountRequestHandler()
  {
    return new StudentAccountRequestHandler(getStudentAccountService());
  }
  public RequestHandler getMyVehiclesRequestHandler()
  {
    return new MyVehiclesRequestHandler(getMyVehiclesService());
  }

  public Logger getLogger()
  {
    return new ConsoleLogger(LogLevel.INFO);
  }

  private static AuthenticationService getAuthenticationService()
  {
    return new AuthServiceImpl(getUserDao());
  }
  public static StudentAccountService getStudentAccountService()
  {
    return new StudentAccountServiceImpl(getReservationDao(), getUserDao());
  }
  private static MyVehiclesService getMyVehiclesService()
  {
    return new MyVehiclesServiceImpl(getVehicleDao());
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
      throw new RuntimeException("Failed to initialize DAO",
          e); // or handle as needed
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
      throw new RuntimeException("Failed to initialize DAO",
          e); // or handle as needed
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
      throw new RuntimeException("Failed to initialize DAO",
          e); // or handle as needed
    }
  }

  static ReservationService getReservationService()
  {
    return new ReservationServiceImpl(getReservationDao(), getVehicleDao());
  }

}