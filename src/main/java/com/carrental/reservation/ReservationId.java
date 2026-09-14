package com.carrental.reservation;

/**
 * ValueObject representing a unique reservation identifier
 */
public record ReservationId(String value) {
    public ReservationId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Reservation ID cannot be empty");
        }
    }
}
