package com.carrental.application;

import com.carrental.application.models.RentVehicleCommand;
import com.carrental.reservation.CustomerId;
import com.carrental.reservation.Reservation;
import com.carrental.reservation.ReservationFacade;
import com.carrental.reservation.ReservationId;
import com.carrental.vehicle.Vehicle;
import com.carrental.vehicle.VehicleFacade;
import com.carrental.vehicle.VehicleId;

import java.util.List;

/**
 * RentalFacade - entry point to the subdomains
 * Orchestrates calls to individual domain facades
 */
public class RentalFacade {
    private final VehicleFacade vehicleFacade;
    private final ReservationFacade reservationFacade;

    public RentalFacade(VehicleFacade vehicleFacade, ReservationFacade reservationFacade) {
        this.vehicleFacade = vehicleFacade;
        this.reservationFacade = reservationFacade;
    }

    // ========== VEHICLE OPERATIONS ==========

    public Vehicle getVehicle(String vehicleId) {
        return vehicleFacade.getVehicle(new VehicleId(vehicleId));
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleFacade.getAvailableVehicles();
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleFacade.getAllVehicles();
    }

    // ========== RESERVATION OPERATIONS ==========

    public ReservationId rentVehicle(RentVehicleCommand command) {
        var vehicle = vehicleFacade.getVehicle(new VehicleId(command.vehicleId()));
        var reservationId = reservationFacade.createReservation(command, vehicle);
        vehicleFacade.markVehicleAsRented(vehicle.getId());
        return reservationId;
    }

    public void cancelRental(String reservationId) {
        var reservation = reservationFacade.getReservation(reservationId);
        reservationFacade.cancelReservation(reservationId);
        vehicleFacade.markVehicleAsAvailable(reservation.getVehicleId());
    }

    public List<Reservation> getCustomerReservations(CustomerId customerId) {
        return reservationFacade.getReservationsByCustomer(customerId);
    }
}
