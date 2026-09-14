package com.carrental.reservation.ports;

import com.carrental.reservation.CustomerId;
import com.carrental.reservation.Reservation;
import com.carrental.reservation.ReservationId;
import com.carrental.reservation.RentalPeriod;
import com.carrental.vehicle.VehicleId;
import java.util.List;
import java.util.Optional;

/**
 * Port (outgoing) - abstraction for accessing reservation storage
 * Implementation will be in the infrastructure layer
 */
public interface ReservationRepository {
    void save(Reservation reservation);

    Optional<Reservation> findById(ReservationId id);

    List<Reservation> findByCustomerId(CustomerId customerId);

    List<Reservation> findConflictingReservations(VehicleId vehicleId, RentalPeriod period);

    void update(Reservation reservation);
}
