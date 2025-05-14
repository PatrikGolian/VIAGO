package networking;

import dtos.Request;
import dtos.Response;
import dtos.error.ErrorResponse;
import dtos.reservation.ReservationDto;
import dtos.reservation.ReservationRequest;
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
import java.util.Arrays;
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

  public MainSocketHandler(Socket clientSocket, ServiceProvider serviceProvider,
      ReadWrite sharedResource)
  {
    this.clientSocket = clientSocket;
    this.serviceProvider = serviceProvider;
    logger = serviceProvider.getLogger();
    this.sharedResource = sharedResource;
  }

  @Override public void run()
  {
    try
    {
      ObjectInputStream incomingData = new ObjectInputStream(
          clientSocket.getInputStream());
      ObjectOutputStream outgoingData = new ObjectOutputStream(
          clientSocket.getOutputStream());
      //handleRequestWithErrorHandling(incomingData, outgoingData);
      while(!clientSocket.isClosed())
      {
        handleRequestWithErrorHandling(incomingData, outgoingData);
      }
    }
    catch (IOException e)
    {
      logger.log(Arrays.toString(e.getStackTrace()), LogLevel.ERROR);
    }

    finally
    {
      try
      {
        clientSocket.close();
      }
      catch (IOException e)
      {
      }
    }
  }

  private void handleRequestWithErrorHandling(ObjectInputStream incomingData,
      ObjectOutputStream outgoingData) throws IOException
  {
    try
    {
      handleRequest(incomingData, outgoingData);
    }
    catch (NotFoundException | InvalidActionException | ValidationException e)
    {
      logger.log(e.getMessage(), LogLevel.INFO);
      ErrorResponse payload = new ErrorResponse(e.getMessage());
      Response error = new Response("ERROR", payload);
      outgoingData.writeObject(error);
    }
    catch (ServerFailureException e)
    {
      logger.log(Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(),
          LogLevel.ERROR);
      ErrorResponse payload = new ErrorResponse(e.getMessage());
      Response error = new Response("SERVER_FAILURE", payload);
      outgoingData.writeObject(error);
    }
    catch (ClassCastException e)
    {
      logger.log(e.getMessage(), LogLevel.INFO);
      ErrorResponse payload = new ErrorResponse("Invalid request");
      Response error = new Response("ERROR", payload);
      outgoingData.writeObject(error);
    }
    catch (Exception e)
    {
      logger.log(Arrays.toString(e.getStackTrace()), LogLevel.ERROR);
      ErrorResponse payload = new ErrorResponse(e.getMessage());
      Response error = new Response("SERVER_FAILURE", payload);
      System.out.println(e.getMessage());
      outgoingData.writeObject(error);
    }
  }

  private void handleRequest(ObjectInputStream incomingData,
      ObjectOutputStream outgoingData)
      throws IOException, ClassNotFoundException, SQLException
  {

    Request request = (Request) incomingData.readObject();
    logger.log("Incoming request: " + request.handler() + "/" + request.action()
        + ". Body: " + request.payload(), LogLevel.INFO);

    logger.log("Handler requested: "+request.handler()+"/"+request.action(), LogLevel.INFO);

    if("reservation".equals(request.handler()) && "subscribe".equals(request.action()))
    {
      reservationSubscribers.add(outgoingData);
      outgoingData.writeObject(new Response("SUCCESS", "subscribed"));
      return;
    }

    if("yourVehicles".equals(request.handler()) && "subscribe".equals(request.action()))
    {
      myVehiclesSubscribers.add(outgoingData);
      outgoingData.writeObject(new Response("SUCCESS", "subscribed"));
    }
    RequestHandler handler = switch (request.handler())
    {
      case "auth" -> serviceProvider.getAuthenticationRequestHandler(sharedResource);
      case "users" -> serviceProvider.getUserRequestHandler(sharedResource);
      case "addVehicle" -> serviceProvider.getAddNewVehicleRequestHandler(sharedResource);
      case "reservation" -> serviceProvider.getReservationRequestHandler(sharedResource);
      case "student" -> serviceProvider.getStudentAccountRequestHandler(sharedResource);
      case "yourVehicles" -> serviceProvider.getMyVehiclesRequestHandler(sharedResource);
      case "allVehicles" -> serviceProvider.getAllVehiclesRequestHandler(sharedResource);
      default -> {
        String msg = String.format(
            "Unknown action '%s' for handler %s; payload was: %s",
            request.action(),
            this.getClass().getSimpleName(),
            request.payload());
        // print to stderr (or use your logger)
        System.err.println(msg);
        // Optionally dump a stack trace here to see the call path:
        new Exception("Stack trace for unknown action").printStackTrace();
        throw new IllegalStateException(
          "Unexpected value: " + request.handler());}
    };

    Object result = handler.handle(request.action(), request.payload());
    //System.out.println("response from server" + result.toString());
    Response response = new Response("SUCCESS", result);
    outgoingData.writeObject(response);

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
      return;
    }

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
      return;
    }
  }
}
