package com.carrental.vehicle;

import com.carrental.vehicle.ports.VehicleRepository;

import java.util.List;

/**
 * VehicleService - domain service for managing vehicles
 */
public class VehicleService {
    private static final String VEHICLE_NOT_FOUND = "Vehicle not found";
    private static final String VEHICLE_IS_NOT_AVAILABLE = "Vehicle is not available";
    private final VehicleRepository vehicleRepository;

    VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle getVehicle(VehicleId vehicleId) {
        var vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new IllegalArgumentException(VEHICLE_NOT_FOUND));
        if (!vehicle.isAvailable()) {
            throw new IllegalStateException(VEHICLE_IS_NOT_AVAILABLE);
        }
        return vehicle;
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findAllAvailable();
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public void markVehicleAsRented(VehicleId vehicleId) {
        var vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException(VEHICLE_NOT_FOUND));
        vehicle.markAsRented();
        vehicleRepository.update(vehicle);
    }

    public void markVehicleAsAvailable(VehicleId vehicleId) {
        var vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException(VEHICLE_NOT_FOUND));
        vehicle.markAsAvailable();
        vehicleRepository.update(vehicle);
    }
}

