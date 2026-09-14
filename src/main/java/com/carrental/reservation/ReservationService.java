package com.carrental.reservation;

import com.carrental.application.models.RentVehicleCommand;
import com.carrental.reservation.ports.ReservationRepository;
import com.carrental.vehicle.Vehicle;

import java.util.List;

/**
 * ReservationService - domain service for managing reservations
 */
public class ReservationService {
    public static final String VEHICLE_IS_ALREADY_RESERVED = "Vehicle is already reserved for this period";
    public static final String RESERVATION_NOT_FOUND = "Reservation not found";
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationId createReservation(RentVehicleCommand command, Vehicle vehicle) {
        var rentalPeriod = new RentalPeriod(command.startDateTime(), command.durationDays());
        var conflicts = reservationRepository
                .findConflictingReservations(vehicle.getId(), rentalPeriod);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException(VEHICLE_IS_ALREADY_RESERVED);
        }

        var reservationId = new ReservationId(java.util.UUID.randomUUID().toString());
        var totalPrice = vehicle.getDailyRate().calculateTotalRate(command.durationDays());
        var reservation = new Reservation(reservationId, vehicle.getId(), new CustomerId(command.customerId()),
                                                   rentalPeriod, totalPrice);
        reservationRepository.save(reservation);
        return reservationId;
    }

    public Reservation getReservation(ReservationId reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException(RESERVATION_NOT_FOUND));
    }

    public List<Reservation> getReservationsByCustomer(CustomerId customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    public void cancelReservation(ReservationId reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(RESERVATION_NOT_FOUND));
        reservation.cancel();
        reservationRepository.update(reservation);
    }
}

