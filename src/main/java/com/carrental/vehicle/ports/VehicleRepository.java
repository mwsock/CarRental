package com.carrental.vehicle.ports;

import com.carrental.vehicle.Vehicle;
import com.carrental.vehicle.VehicleId;
import java.util.List;
import java.util.Optional;

/**
 * Port (outgoing) - abstraction for accessing vehicle storage
 * Implementation will be in the infrastructure layer
 */
public interface VehicleRepository {

    Optional<Vehicle> findById(VehicleId id);

    List<Vehicle> findAll();

    List<Vehicle> findAllAvailable();

    void update(Vehicle vehicle);
}
