//Andrew Nguyen's Tests
package com.example.sprintproject;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.sprintproject.model.Reservation;

public class ReservationTest {

    private Reservation reservation;

    @Before
    public void setUp() {
        // Initializing a new reservation instance with sample data
        reservation = new Reservation("1", "Paris", "www.hotel.com", "12/01/2024 14:00");
    }

    @Test
    public void testConstructor() {
        // Testing constructor initialization
        assertEquals("1", reservation.getId());
        assertEquals("Paris", reservation.getLocation());
        assertEquals("www.hotel.com", reservation.getWebsite());
        assertEquals("12/01/2024 14:00", reservation.getReservationTime());
        assertEquals(0, reservation.getRating()); // Default rating should be 0
    }

    @Test
    public void testGettersAndSetters() {
        // Test the getter values
        assertEquals("1", reservation.getId());
        assertEquals("Paris", reservation.getLocation());
        assertEquals("www.hotel.com", reservation.getWebsite());
        assertEquals("12/01/2024 14:00", reservation.getReservationTime());
        assertEquals(0, reservation.getRating()); // Default rating

        // Set new values for the reservation fields
        reservation.setId("2");
        reservation.setRating(5);

        // Test the setter values
        assertEquals("2", reservation.getId());
        assertEquals(5, reservation.getRating());
    }

    @Test
    public void testEmptyConstructor() {
        // Create a new reservation instance using the empty constructor
        Reservation emptyReservation = new Reservation();

        // Assert that all values are initially null or default
        assertNull(emptyReservation.getId());
        assertNull(emptyReservation.getLocation());
        assertNull(emptyReservation.getWebsite());
        assertNull(emptyReservation.getReservationTime());
        assertEquals(0, emptyReservation.getRating()); // Default rating should be 0
    }
}
