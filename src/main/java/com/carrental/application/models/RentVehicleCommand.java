package com.carrental.application.models;

import java.time.LocalDateTime;

public record RentVehicleCommand(String vehicleId, String customerId, LocalDateTime startDateTime, int durationDays) {
}
