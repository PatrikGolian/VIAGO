package dtos.user;

import java.io.Serializable;

public record UpdatePasswordRequest(String email, String oldPassword, String newPassword) implements Serializable
{
    @Override
    public String toString()
    {
        return "UpdatePasswordRequest{" +
                "email='" + email + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
