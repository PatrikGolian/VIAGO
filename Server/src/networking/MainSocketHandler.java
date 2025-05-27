package networking;

import dtos.Request;
import dtos.Response;
import dtos.auth.RegisterUserDto;
import dtos.auth.RegisterUserRequest;
import dtos.error.ErrorResponse;
import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
import dtos.user.BlackListDto;
import dtos.user.BlacklistUserRequest;
import dtos.vehicle.*;
import model.exceptions.ServerFailureException;
import model.exceptions.ValidationException;
import model.exceptions.NotFoundException;
import networking.exceptions.InvalidActionException;
import networking.readerswriters.ReadWrite;
import networking.requesthandlers.RequestHandler;
import startup.ServiceProvider;
import utilities.logging.LogLevel;
import utilities.logging.Logger;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainSocketHandler implements Runnable
{
  private final Socket clientSocket;
  private final ServiceProvider serviceProvider;
  private final Logger logger;
  private final ReadWrite sharedResource;
  private static final List<ObjectOutputStream> reservationSubscribers = new CopyOnWriteArrayList<>();
  private static final List<ObjectOutputStream> myVehiclesSubscribers = new CopyOnWriteArrayList<>();
  private static final List<ObjectOutputStream> allVehiclesSubscribers = new CopyOnWriteArrayList<>();
  private static final List<ObjectOutputStream> viewUsersSubscribers = new CopyOnWriteArrayList<>();
  private static final List<ObjectOutputStream> blacklistSubscribers = new CopyOnWriteArrayList<>();
  private static final List<ObjectOutputStream> studentAccountSubscribers = new CopyOnWriteArrayList<>();

  public MainSocketHandler(Socket clientSocket, ServiceProvider serviceProvider,
      ReadWrite sharedResource)
  {
    this.clientSocket = clientSocket;
    this.serviceProvider = serviceProvider;
    logger = serviceProvider.getLogger();
    this.sharedResource = sharedResource;
  }

  @Override
  public void run() {
    try (ObjectInputStream  in  = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

      while (true) {
        Request req = (Request) in.readObject();

        if (isSubscribeRequest(req)) {
          registerSubscriber(req, out);
          out.writeObject(new Response("SUCCESS", "subscribed"));
          out.flush();
          continue;
        }

        handleRequestWithErrorHandling(req, out);
        broadcastIfNeeded(req);
        break;
      }

    }
    catch (EOFException eof)
    {

    }
    catch (Exception e)
    {
      logger.log(e.getMessage(), LogLevel.ERROR);
    }
    finally
    {
      try { clientSocket.close(); } catch (IOException ignore){}
    }
  }

  private void broadcastIfNeeded(Request request)
  {
    if("reservation".equals(request.handler()) && "reserve".equals(request.action()))
    {
      ReservationRequest reservationRequest = (ReservationRequest) request.payload();
      ReservationDto reservationDto = new ReservationDto
          (
              reservationRequest.vehicleId(),
              reservationRequest.vehicleType(),
              reservationRequest.ownerEmail(),
              reservationRequest.reservedByEmail(),
              reservationRequest.startDate(),
              reservationRequest.endDate(),
              reservationRequest.price()
          );
      Response push = new Response("RESERVATION_ADDED", reservationDto);

      for(ObjectOutputStream subscriber : reservationSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          reservationSubscribers.remove(subscriber);
        }
      }
      for(ObjectOutputStream subscriber : myVehiclesSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          myVehiclesSubscribers.remove(subscriber);
        }
      }
      for(ObjectOutputStream subscriber : allVehiclesSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          allVehiclesSubscribers.remove(subscriber);
        }
      }
      return;
    }

    if("addVehicle".equals(request.handler()) && "addBike".equals(request.action()))
    {
      AddNewBikeRequest addNewBikeRequest = (AddNewBikeRequest) request.payload();
      AddNewBikeDto addNewBikeDto = new AddNewBikeDto
          (
              addNewBikeRequest.id(),
              addNewBikeRequest.type(),
              addNewBikeRequest.brand(),
              addNewBikeRequest.model(),
              addNewBikeRequest.condition(),
              addNewBikeRequest.color(),
              addNewBikeRequest.pricePerDay(),
              addNewBikeRequest.bikeType(),
              addNewBikeRequest.ownerEmail(),
              addNewBikeRequest.state()
          );
      Response push = new Response("BIKE_ADDED", addNewBikeDto);

      for(ObjectOutputStream subscriber : reservationSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          reservationSubscribers.remove(subscriber);
        }
      }
      for(ObjectOutputStream subscriber : allVehiclesSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          allVehiclesSubscribers.remove(subscriber);
        }
      }
      return;
    }

    if("addVehicle".equals(request.handler()) && "addScooter".equals(request.action()))
    {
      AddNewScooterRequest addNewScooterRequest = (AddNewScooterRequest) request.payload();
      AddNewScooterDto addNewScooterDto = new AddNewScooterDto
          (
              addNewScooterRequest.id(),
              addNewScooterRequest.type(),
              addNewScooterRequest.brand(),
              addNewScooterRequest.model(),
              addNewScooterRequest.condition(),
              addNewScooterRequest.color(),
              addNewScooterRequest.pricePerDay(),
              addNewScooterRequest.maxSpeed(),
              addNewScooterRequest.oneChargeRange(),
              addNewScooterRequest.ownerEmail(),
              addNewScooterRequest.state()
          );
      Response push = new Response("SCOOTER_ADDED", addNewScooterDto);

      for(ObjectOutputStream subscriber : reservationSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          reservationSubscribers.remove(subscriber);
        }
      }
      for(ObjectOutputStream subscriber : allVehiclesSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          allVehiclesSubscribers.remove(subscriber);
        }
      }
      return;
    }

    if("addVehicle".equals(request.handler()) && "addEBike".equals(request.action()))
    {
      AddNewEBikeRequest addNewEBikeRequest =  (AddNewEBikeRequest) request.payload();
      AddNewEBikeDto addNewEBikeDto =  new AddNewEBikeDto(
          addNewEBikeRequest.id(),
          addNewEBikeRequest.type(),
          addNewEBikeRequest.brand(),
          addNewEBikeRequest.model(),
          addNewEBikeRequest.condition(),
          addNewEBikeRequest.color(),
          addNewEBikeRequest.pricePerDay(),
          addNewEBikeRequest.maxSpeed(),
          addNewEBikeRequest.oneChargeRange(),
          addNewEBikeRequest.bikeType(),
          addNewEBikeRequest.ownerEmail(),
          addNewEBikeRequest.state()
      );
      Response push = new Response("EBIKE_ADDED", addNewEBikeDto);

      for(ObjectOutputStream subscriber : reservationSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          reservationSubscribers.remove(subscriber);
        }
      }
      for(ObjectOutputStream subscriber : allVehiclesSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          allVehiclesSubscribers.remove(subscriber);
        }
      }
      return;
    }

    if("auth".equals(request.handler()) && "register".equals(request.action()))
    {
      RegisterUserRequest registerUserRequest = (RegisterUserRequest) request.payload();
      RegisterUserDto registerUserDto = new RegisterUserDto(
          registerUserRequest.email(),
          registerUserRequest.password(),
          registerUserRequest.firstName(),
          registerUserRequest.lastName());
      Response push = new Response("USER_ADDED", registerUserDto);

      for(ObjectOutputStream subscriber : viewUsersSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          viewUsersSubscribers.remove(subscriber);
        }
      }
      return;
    }

    if("users".equals(request.handler()) && "blacklist".equals(request.action()))
    {
      BlacklistUserRequest blacklistUserRequest = (BlacklistUserRequest)request.payload();
      BlackListDto blackListDto = new BlackListDto
          (
              blacklistUserRequest.email(),
              blacklistUserRequest.reason()
          );
      Response push = new Response("USER_BLACKLISTED", blackListDto);
      for(ObjectOutputStream subscriber : blacklistSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          blacklistSubscribers.remove(subscriber);
        }
      }
      for(ObjectOutputStream subscriber : reservationSubscribers)
      {
        try{
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          reservationSubscribers.remove(subscriber);
        }
      }
      for(ObjectOutputStream subscriber : allVehiclesSubscribers)
      {
        try{
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          allVehiclesSubscribers.remove(subscriber);
        }
      }
      for(ObjectOutputStream subscriber : studentAccountSubscribers)
      {
        try
        {
          subscriber.writeObject(push);
        }
        catch (IOException e)
        {
          studentAccountSubscribers.remove(subscriber);
        }
      }
      return;
    }
  }

  private Object handleOne(Request req)
      throws SQLException,
      InvalidActionException,
      NotFoundException,
      ValidationException
  {
    RequestHandler handler = switch (req.handler())
    {
      case "auth"         -> serviceProvider.getAuthenticationRequestHandler(sharedResource);
      case "users"        -> serviceProvider.getUserRequestHandler(sharedResource);
      case "addVehicle"   -> serviceProvider.getAddNewVehicleRequestHandler(sharedResource);
      case "reservation"  -> serviceProvider.getReservationRequestHandler(sharedResource);
      case "student"      -> serviceProvider.getStudentAccountRequestHandler(sharedResource);
      case "yourVehicles" -> serviceProvider.getMyVehiclesRequestHandler(sharedResource);
      case "allVehicles"  -> serviceProvider.getAllVehiclesRequestHandler(sharedResource);
      default -> throw new InvalidActionException("Unknown handler: " + req.handler(), req.action());
    };
    try
    {
      return handler.handle(req.action(), req.payload());
    }
    catch (Exception e)
    {
      throw new ValidationException(e.getMessage());
    }

  }

  private void registerSubscriber(Request request, ObjectOutputStream outgoing)
  {
    switch (request.handler()) {
      case "reservation" : reservationSubscribers.add(outgoing); break;
      case "yourVehicles": myVehiclesSubscribers.add(outgoing); break;
      case "allVehicles": allVehiclesSubscribers.add(outgoing); break;
      case "users":
      {
        viewUsersSubscribers.add(outgoing);
        blacklistSubscribers.add(outgoing);
      } break;
      case "student" : studentAccountSubscribers.add(outgoing); break;
    }
  }

  private boolean isSubscribeRequest(Request request)
  {
    return ("reservation".equals(request.handler()) && "subscribe".equals(request.action()))
        || ("yourVehicles".equals(request.handler()) && "subscribe".equals(request.action()))
        || ("allVehicles".equals(request.handler()) && "subscribe".equals(request.action()))
        || ("users".equals(request.handler()) && "subscribe".equals(request.action()))
        || ("student".equals(request.handler()) && "subscribe".equals(request.action()));
  }

  private void handleRequestWithErrorHandling(Request req,
      ObjectOutputStream out) throws IOException {
    try {
      Object result = handleOne(req);
      out.writeObject(new Response("SUCCESS", result));
      out.flush();
    } catch (ValidationException|NotFoundException e) {
      out.writeObject(new Response("ERROR", new ErrorResponse(e.getMessage())));
    } catch (ServerFailureException e) {
      out.writeObject(new Response("SERVER_FAILURE", new ErrorResponse(e.getMessage())));
    } catch (ClassCastException e) {
      out.writeObject(new Response("ERROR", new ErrorResponse("Invalid request")));
    } catch (Exception e) {
      out.writeObject(new Response("SERVER_FAILURE", new ErrorResponse(e.getMessage())));
    } finally {
      try { out.flush(); } catch(IOException ignore){}
    }
  }
}
