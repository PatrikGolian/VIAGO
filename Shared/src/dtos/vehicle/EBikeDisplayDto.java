package dtos.vehicle;

public record EBikeDisplayDto(int id, String type, String brand, String model,
                              double pricePerDay, String state, String condition,
                              String color, String ownerEmail, String bikeType,
                              int maxSpeed, int oneChargeRange
) implements VehicleDisplayDto {}
