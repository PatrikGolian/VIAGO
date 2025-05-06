package dtos.vehicle;

import java.io.Serializable;

public record VehicleDisplayDto(String type, String brand, String model, double pricePerDay, String state) implements Serializable
{

}
