package com.carrental.reservation;

import com.carrental.vehicle.VehicleId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Reservation - Aggregate Root in ReservationDomain
 * Entity representing a vehicle reservation for a specified period
 */
@Data
public class Reservation {
    private final ReservationId id;
    private final VehicleId vehicleId;
    private final CustomerId customerId;
    private final RentalPeriod rentalPeriod;
    private final BigDecimal totalPrice;
    private ReservationStatus status;

    private enum ReservationStatus {
        ACTIVE,
        CANCELLED
    }

    public Reservation(ReservationId id, VehicleId vehicleId, CustomerId customerId,
                       RentalPeriod rentalPeriod, BigDecimal totalPrice) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.rentalPeriod = rentalPeriod;
        this.totalPrice = totalPrice;
        this.status = ReservationStatus.ACTIVE;
    }

    public void cancel() {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("PENDING or cancelled reservations cannot be cancelled");
        }
        this.status = ReservationStatus.CANCELLED;
    }
}

