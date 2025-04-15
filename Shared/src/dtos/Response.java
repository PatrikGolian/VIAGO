package dtos;

import java.io.Serializable;

public record Response(String status, Object payload) implements Serializable
{
}
