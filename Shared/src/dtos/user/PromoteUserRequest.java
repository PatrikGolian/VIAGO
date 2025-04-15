package dtos.user;

import java.io.Serializable;

public record PromoteUserRequest(String email) implements Serializable
{
    @Override
    public String toString()
    {
        return "PromoteUserRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}
