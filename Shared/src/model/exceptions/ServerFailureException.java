package model.exceptions;

public class ServerFailureException extends RuntimeException
{
  public ServerFailureException(String message)
  {
    super(message);
  }
}
