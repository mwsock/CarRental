package com.carrental.vehicle;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class VehicleFacade {

    private final VehicleService vehicleService;

    public Vehicle getVehicle(VehicleId vehicleId) {
        return vehicleService.getVehicle(vehicleId);
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.getAvailableVehicles();
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    public void markVehicleAsRented(VehicleId vehicleId) {
        vehicleService.markVehicleAsRented(vehicleId);
    }

    public void markVehicleAsAvailable(VehicleId vehicleId) {
        vehicleService.markVehicleAsAvailable(vehicleId);
    }
}
