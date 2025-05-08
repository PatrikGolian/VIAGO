package dtos.vehicle;

public record BikeDisplayDto(int id, String type, String brand, String model,
                             double pricePerDay, String state, String condition,
                             String color, String ownerEmail, String bikeType
) implements VehicleDisplayDto {}
