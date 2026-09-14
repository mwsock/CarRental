package com.carrental.vehicle;

import com.carrental.vehicle.ports.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleFacadeTest {

    @Mock
    private VehicleRepository vehicleRepository;
    private VehicleFacade facade;

    @BeforeEach
    void setUp() {
        var vehicleService = new VehicleService(vehicleRepository);
        facade = new VehicleFacade(vehicleService);
    }

    @Test
    void shouldListAvailableVehicles() {
        // Given
        List<Vehicle> availableVehicles = new ArrayList<>();
        availableVehicles.add(new Vehicle(new VehicleId("v1"), VehicleType.SUV, new LicensePlate("WA001"), "Toyota", "Land Cruiser", 2023, new DailyRate(BigDecimal.valueOf(150.00))));
        availableVehicles.add(new Vehicle(new VehicleId("v2"), VehicleType.SEDAN, new LicensePlate("WA002"), "BMW", "3 Series", 2022, new DailyRate(BigDecimal.valueOf(100.00))));

        when(vehicleRepository.findAllAvailable()).thenReturn(availableVehicles);

        // When
        var result = facade.getAvailableVehicles();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(v -> v.getType() == VehicleType.SUV));
        assertTrue(result.stream().anyMatch(v -> v.getType() == VehicleType.SEDAN));
    }

    @Test
    void shouldListAllVehicles() {
        // Given
        List<Vehicle> allVehicles = new ArrayList<>();
        allVehicles.add(new Vehicle(new VehicleId("v1"), VehicleType.SUV, new LicensePlate("WA001"), "Toyota", "Land Cruiser", 2023, new DailyRate(BigDecimal.valueOf(150.00))));
        allVehicles.add(new Vehicle(new VehicleId("v2"), VehicleType.SEDAN, new LicensePlate("WA002"), "BMW", "3 Series", 2022, new DailyRate(BigDecimal.valueOf(100.00))));

        when(vehicleRepository.findAll()).thenReturn(allVehicles);

        // When
        var result = facade.getAllVehicles();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldGetVehicleById() {
        // Given
        var vehicleId = new VehicleId("vehicle-001");
        Vehicle vehicle = new Vehicle(vehicleId, VehicleType.SUV, new LicensePlate("WA12345"), "Toyota", "Land Cruiser", 2023, new DailyRate(BigDecimal.valueOf(150.00)));

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        // When
        var result = facade.getVehicle(vehicleId);

        // Then
        assertEquals(vehicleId, result.getId());
        assertEquals(VehicleType.SUV, result.getType());
    }
}