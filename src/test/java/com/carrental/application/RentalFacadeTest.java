package com.carrental.application;

import com.carrental.application.models.RentVehicleCommand;
import com.carrental.reservation.*;
import com.carrental.reservation.CustomerId;
import com.carrental.reservation.RentalPeriod;
import com.carrental.reservation.ReservationId;
import com.carrental.vehicle.*;
import com.carrental.vehicle.DailyRate;
import com.carrental.vehicle.LicensePlate;
import com.carrental.vehicle.VehicleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalFacadeTest {
    @Mock
    private VehicleService vehicleService;

    @Mock
    private ReservationService reservationService;

    private RentalFacade rentalFacade;

    @BeforeEach
    void setUp() {
        var reservationFacade = new ReservationFacade(reservationService);
        var vehicleFacade = new VehicleFacade(vehicleService);
        rentalFacade = new RentalFacade(vehicleFacade, reservationFacade);
    }

    @Test
    void shouldRentAvailableVehicle() {
        // Given
        var vehicleId = new VehicleId("vehicle-001");
        var customerId = "customer-001";
        var startDateTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        var durationDays = 3;

        Vehicle vehicle = new Vehicle(vehicleId, VehicleType.SUV, new LicensePlate("WA12345"), "Toyota", "Land Cruiser", 2023, new DailyRate(BigDecimal.valueOf(150.00)));

        ReservationId reservationId = new ReservationId("res-001");
        RentVehicleCommand command = new RentVehicleCommand(vehicleId.value(), customerId, startDateTime, durationDays);

        when(vehicleService.getVehicle(vehicleId)).thenReturn(vehicle);
        when(reservationService.createReservation(command, vehicle)).thenReturn(reservationId);

        // When
        ReservationId result = rentalFacade.rentVehicle(command);

        // Then
        assertEquals(reservationId, result);
    }

    @Test
    void shouldNotRentUnavailableVehicle() {
        // Given
        var vehicleId = new VehicleId("vehicle-001");
        var customerId = "customer-001";
        var startDateTime = LocalDateTime.now().plusDays(1);
        var durationDays = 3;
        var command = new RentVehicleCommand(vehicleId.value(), customerId, startDateTime, durationDays);

        when(vehicleService.getVehicle(vehicleId)).thenThrow(new IllegalStateException());

        // When & Then
        assertThrows(IllegalStateException.class, () -> rentalFacade.rentVehicle(command));
    }


    @Test
    void shouldCancelRental() {
        // Given
        var reservationId = "res-001";
        var vehicleId = new VehicleId("vehicle-001");
        var customerId = new CustomerId("customer-001");
        var resId = new ReservationId(reservationId);

        Reservation reservation = new Reservation(resId, vehicleId, customerId, new RentalPeriod(LocalDateTime.now().plusDays(1), 3), BigDecimal.valueOf(300.00));

        when(reservationService.getReservation(resId)).thenReturn(reservation);

        // When
        rentalFacade.cancelRental(reservationId);

        // Then
        verify(reservationService, times(1)).cancelReservation(resId);
        verify(vehicleService, times(1)).markVehicleAsAvailable(vehicleId);
    }

    @Test
    void shouldListAvailableVehicles() {
        // Given
        List<Vehicle> availableVehicles = new ArrayList<>();
        availableVehicles.add(new Vehicle(new VehicleId("v1"), VehicleType.SUV, new LicensePlate("WA001"), "Toyota", "Land Cruiser", 2023, new DailyRate(BigDecimal.valueOf(150.00))));
        availableVehicles.add(new Vehicle(new VehicleId("v2"), VehicleType.SEDAN, new LicensePlate("WA002"), "BMW", "3 Series", 2022, new DailyRate(BigDecimal.valueOf(100.00))));

        when(vehicleService.getAvailableVehicles()).thenReturn(availableVehicles);

        // When
        var result = rentalFacade.getAvailableVehicles();

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

        when(vehicleService.getAllVehicles()).thenReturn(allVehicles);

        // When
        var result = rentalFacade.getAllVehicles();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldGetVehicleById() {
        // Given
        var vehicleId = new VehicleId("vehicle-001");
        Vehicle vehicle = new Vehicle(vehicleId, VehicleType.SUV, new LicensePlate("WA12345"), "Toyota", "Land Cruiser", 2023, new DailyRate(BigDecimal.valueOf(150.00)));

        when(vehicleService.getVehicle(vehicleId)).thenReturn(vehicle);

        // When
        var result = rentalFacade.getVehicle(vehicleId.value());

        // Then
        assertEquals(vehicleId, result.getId());
        assertEquals(VehicleType.SUV, result.getType());
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        // Given
        var vehicleId = new VehicleId("non-existent");

        when(vehicleService.getVehicle(vehicleId)).thenThrow(new IllegalArgumentException("Vehicle not found"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> rentalFacade.getVehicle(vehicleId.value()));
    }

    @Test
    void shouldNotRentVehicleWhenAlreadyReserved() {
        // Given
        var vehicleId = new VehicleId("vehicle-001");
        var customerId = "customer-001";
        var startDateTime = LocalDateTime.now().plusDays(1);
        var durationDays = 3;
        var command = new RentVehicleCommand(vehicleId.value(), customerId, startDateTime, durationDays);

        Vehicle vehicle = new Vehicle(vehicleId, VehicleType.SUV, new LicensePlate("WA12345"), "Toyota", "Land Cruiser", 2023, new DailyRate(BigDecimal.valueOf(150.00)));

        when(vehicleService.getVehicle(vehicleId)).thenReturn(vehicle);
        when(reservationService.createReservation(command, vehicle)).thenThrow(new IllegalStateException("Vehicle is already reserved for this period"));

        // When & Then
        assertThrows(IllegalStateException.class, () -> rentalFacade.rentVehicle(command));
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonExistentReservation() {
        // Given
        var reservationId = new ReservationId("non-existent");

        when(reservationService.getReservation(reservationId)).thenThrow(new IllegalArgumentException("Reservation not found"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> rentalFacade.cancelRental("non-existent"));
    }

    @Test
    void shouldThrowExceptionWhenCancellingAlreadyCancelledReservation() {
        // Given
        var customerId = new CustomerId("customer-001");
        var reservationId = new ReservationId("res-001");
        var vehicleId = new VehicleId("vehicle-001");
        var rentalPeriod = new RentalPeriod(LocalDateTime.now().plusDays(1), 3);

        Reservation reservation = new Reservation(reservationId, vehicleId, customerId, rentalPeriod, BigDecimal.valueOf(300.00));
        reservation.cancel();

        when(reservationService.getReservation(reservationId)).thenReturn(reservation);
        doThrow(new IllegalStateException("Cancelled reservations cannot be cancelled")).when(reservationService).cancelReservation(reservationId);

        // When & Then
        assertThrows(IllegalStateException.class, () -> rentalFacade.cancelRental("res-001"));
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomerReservations() {
        // Given
        var customerId = new CustomerId("customer-001");

        when(reservationService.getReservationsByCustomer(customerId)).thenReturn(new ArrayList<>());

        // When
        var result = rentalFacade.getCustomerReservations(customerId);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnMultipleCustomerReservations() {
        // Given
        var customerId = new CustomerId("customer-001");
        var vehicleId1 = new VehicleId("vehicle-001");
        var vehicleId2 = new VehicleId("vehicle-002");

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(new ReservationId("res-001"), vehicleId1, customerId, new RentalPeriod(LocalDateTime.now().plusDays(1), 3), BigDecimal.valueOf(300.00)));
        reservations.add(new Reservation(new ReservationId("res-002"), vehicleId2, customerId, new RentalPeriod(LocalDateTime.now().plusDays(10), 5), BigDecimal.valueOf(500.00)));

        when(reservationService.getReservationsByCustomer(customerId)).thenReturn(reservations);

        // When
        var result = rentalFacade.getCustomerReservations(customerId);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getVehicleId().equals(vehicleId1)));
        assertTrue(result.stream().anyMatch(r -> r.getVehicleId().equals(vehicleId2)));
    }
}
