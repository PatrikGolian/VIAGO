package networking;

import dtos.Request;
import dtos.Response;
import dtos.error.ErrorResponse;
import model.exceptions.ServerFailureException;
import model.exceptions.ValidationException;
import model.exceptions.NotFoundException;
import networking.exceptions.InvalidActionException;
import networking.requesthandlers.RequestHandler;
import startup.ServiceProvider;
import utilities.logging.LogLevel;
import utilities.logging.Logger;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;

public class MainSocketHandler implements Runnable
{

    // I have specific SocketHandlers, generally per entity type, but not necessarily.
    // It is to split things out a little bit. Make parallel work easier.
    // The responsibility of this class is to
    // * receive the request from the client
    // * figure out which specific handler to pass the request to
    // * handle any exceptions thrown from anywhere in the server
    // * respond back to the client with either a success or failure message

    private final Socket clientSocket;
    private final ServiceProvider serviceProvider;
    private final Logger logger;

    public MainSocketHandler(Socket clientSocket, ServiceProvider serviceProvider)
    {
        this.clientSocket = clientSocket;
        this.serviceProvider = serviceProvider;
        logger = serviceProvider.getLogger();
    }

    @Override
    public void run()
    {
        try
        {
            ObjectInputStream incomingData = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outgoingData = new ObjectOutputStream(clientSocket.getOutputStream());
            handleRequestWithErrorHandling(incomingData, outgoingData);
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

    private void handleRequestWithErrorHandling(ObjectInputStream incomingData, ObjectOutputStream outgoingData) throws IOException
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
        catch(ServerFailureException e){
            logger.log(Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(), LogLevel.ERROR);
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
            e.printStackTrace();
            logger.log(Arrays.toString(e.getStackTrace()), LogLevel.ERROR);
            ErrorResponse payload = new ErrorResponse(e.getMessage());
            Response error = new Response("SERVER_FAILURE", payload);
            outgoingData.writeObject(error.toString());
        }
    }

    private void handleRequest(ObjectInputStream incomingData, ObjectOutputStream outgoingData)
        throws IOException, ClassNotFoundException, SQLException
    {
        Request request = (Request) incomingData.readObject();
        logger.log("Incoming request: " + request.handler() + "/" + request.action() + ". Body: " + request.payload(), LogLevel.INFO);

        RequestHandler handler = switch (request.handler())
        {
            case "auth" -> serviceProvider.getAuthenticationRequestHandler();
            case "users" -> serviceProvider.getUserRequestHandler();
            case "addVehicle" -> serviceProvider.getAddNewVehicleRequestHandler();
            case "reservation" -> serviceProvider.getReservationRequestHandler();
            default -> throw new IllegalStateException("Unexpected value: " + request.handler());
        };

        Object result = handler.handle(request.action(), request.payload());
        //System.out.println("response from server" + result.toString());
        Response response = new Response("SUCCESS", result);
        outgoingData.writeObject(response);
    }
}
