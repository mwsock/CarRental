package com.carrental.vehicle;

import lombok.Getter;

/**
 * Enum for available vehicle types
 */
@Getter
public enum VehicleType {
    SUV("Special Utility Vehicle", 5),
    SEDAN("Sedan", 5),
    VAN("Van", 7);

    private final String description;
    private final int seats;

    VehicleType(String description, int seats) {
        this.description = description;
        this.seats = seats;
    }
}
