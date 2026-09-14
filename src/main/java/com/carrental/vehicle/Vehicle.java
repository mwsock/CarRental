package com.carrental.vehicle;

import lombok.Data;

/**
 * Vehicle - Aggregate Root in VehicleDomain
 * Entity representing a vehicle available for rental
 */
@Data
public class Vehicle {
    private final VehicleId id;
    private final VehicleType type;
    private final LicensePlate licensePlate;
    private final String brand;
    private final String model;
    private final int yearOfManufacture;
    private final DailyRate dailyRate;
    private VehicleStatus status;

    public enum VehicleStatus {
        AVAILABLE,
        RENTED,
        UNDER_MAINTENANCE
    }

    public Vehicle(VehicleId id, VehicleType type, LicensePlate licensePlate,
                   String brand, String model, int yearOfManufacture,
                   DailyRate dailyRate) {
        this.id = id;
        this.type = type;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.yearOfManufacture = yearOfManufacture;
        this.dailyRate = dailyRate;
        this.status = VehicleStatus.AVAILABLE;
    }

    void markAsRented() {
        if (status != VehicleStatus.AVAILABLE) {
            throw new IllegalStateException("Vehicle is not available for rental");
        }
        this.status = VehicleStatus.RENTED;
    }

    void markAsAvailable() {
        this.status = VehicleStatus.AVAILABLE;
    }

    boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }
}

