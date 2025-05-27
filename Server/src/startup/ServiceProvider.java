package startup;

import networking.readerswriters.ReadWrite;
import networking.requesthandlers.*;
import persistence.reservation.ReservationDao;
import persistence.reservation.ReservationPostgresDao;
import persistence.user.UserDao;
import persistence.user.UserPostgresDao;
import services.allvehicles.AllVehiclesService;
import services.allvehicles.AllVehiclesServiceImpl;
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
  public RequestHandler getAuthenticationRequestHandler(ReadWrite sharedResource)
  {
    return new AuthRequestHandler(getAuthenticationService(), sharedResource);
  }

  public RequestHandler getUserRequestHandler(ReadWrite sharedResource)
  {
    return new UserRequestHandler(getUserService(), sharedResource);
  }

  public RequestHandler getAddNewVehicleRequestHandler(ReadWrite sharedResource)
  {
    return new AddNewVehicleRequestHandler(getVehicleService(), sharedResource);
  }

  public RequestHandler getReservationRequestHandler(ReadWrite sharedResource)
  {
    return new ReservationRequestHandler(getReservationService(),
        getVehicleService(), sharedResource);
  }

  public RequestHandler getStudentAccountRequestHandler(ReadWrite sharedResource)
  {
    return new StudentAccountRequestHandler(getStudentAccountService(), sharedResource);
  }
  public RequestHandler getMyVehiclesRequestHandler(ReadWrite sharedResource)
  {
    return new MyVehiclesRequestHandler(getMyVehiclesService(), sharedResource);
  }
  public RequestHandler getAllVehiclesRequestHandler(ReadWrite sharedResource)
  {
    return new AllVehiclesRequestHandler(getAllVehiclesService(), sharedResource);
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

  private static AllVehiclesService getAllVehiclesService()
  {
    return new AllVehiclesServiceImpl(getVehicleDao());
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
          e);
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
          e);
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
          e);
    }
  }

  static ReservationService getReservationService()
  {
    return new ReservationServiceImpl(getReservationDao(), getVehicleDao());
  }

}