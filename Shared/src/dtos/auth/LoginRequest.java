package dtos.auth;

import java.io.Serializable;

public record LoginRequest(String email, String password) implements Serializable
{
    @Override
    public String toString()
    {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
