package dtos.vehicle;

public record ScooterDisplayDto( int id, String type, String brand, String model,
                                 double pricePerDay, String state, String condition,
                                 String color, String ownerEmail, int maxSpeed, int oneChargeRange
) implements VehicleDisplayDto {}
