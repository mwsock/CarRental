package com.carrental.vehicle;

/**
 * ValueObject representing a vehicle license plate number
 */
public record LicensePlate(String number) {
    public LicensePlate {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("License plate number cannot be empty");
        }
        number = number.toUpperCase();
    }
}

