package model.exceptions;

public class NoSuchServiceException extends RuntimeException
{
    public NoSuchServiceException(String serviceName)
    {
        super(serviceName);
    }
}
