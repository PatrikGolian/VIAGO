package networking;

import dtos.Request;
import dtos.Response;
import dtos.error.ErrorResponse;
import utils.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketService
{
    public static Object sendRequest(Request request)
    {
        try (Socket socket = new Socket("localhost", 2910);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()))
        {
            outputStream.writeObject(request);
            Response response = (Response) inputStream.readObject();
            switch (response.status())
            {
                case "SUCCESS" ->
                {
                    return response.payload();
                }
                case "ERROR" -> throw new RuntimeException(((ErrorResponse) response.payload()).errorMessage());
                default -> throw new RuntimeException("Unknown server status code: " + response.status());
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not connect to server!");
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Invalid response from server.");
        }
    }
}
