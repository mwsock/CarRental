package com.carrental.reservation;

import com.carrental.application.models.RentVehicleCommand;
import com.carrental.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReservationFacade {

    private final ReservationService reservationService;

    public ReservationId createReservation(RentVehicleCommand command, Vehicle vehicle) {
        return reservationService.createReservation(command, vehicle);
    }

    public Reservation getReservation(String reservationId) {
        return reservationService.getReservation(new ReservationId(reservationId));
    }

    public List<Reservation> getReservationsByCustomer(CustomerId customerId) {
        return reservationService.getReservationsByCustomer(customerId);
    }

    public void cancelReservation(String reservationId) {
        reservationService.cancelReservation(new ReservationId(reservationId));
    }
}
