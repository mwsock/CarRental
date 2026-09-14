package com.carrental.reservation;

import com.carrental.reservation.ports.ReservationRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationFacade facade;

    @BeforeEach
    void setUp() {
        var reservationService = new ReservationService(reservationRepository);
        facade = new ReservationFacade(reservationService);
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonExistentReservation() {
        // Given
        var reservationId = new ReservationId("non-existent");

        when(reservationRepository.findById(reservationId)).thenThrow(new IllegalArgumentException("Reservation not found"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> facade.cancelReservation("non-existent"));
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

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        // When & Then
        assertThrows(IllegalStateException.class, () -> facade.cancelReservation("res-001"));
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomerReservations() {
        // Given
        var customerId = new CustomerId("customer-001");

        when(reservationRepository.findByCustomerId(customerId)).thenReturn(new ArrayList<>());

        // When
        var result = facade.getReservationsByCustomer(customerId);

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

        when(reservationRepository.findByCustomerId(customerId)).thenReturn(reservations);

        // When
        var result = facade.getReservationsByCustomer(customerId);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getVehicleId().equals(vehicleId1)));
        assertTrue(result.stream().anyMatch(r -> r.getVehicleId().equals(vehicleId2)));
    }
}