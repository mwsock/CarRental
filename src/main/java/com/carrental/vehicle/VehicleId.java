package com.carrental.vehicle;

/**
 * ValueObject representing a vehicle identifier
 */
public record VehicleId(String value) {
    public VehicleId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty");
        }
    }
}

