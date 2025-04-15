package dtos;

import java.io.Serializable;

public record Request(String handler, String action, Object payload) implements Serializable
{
}

