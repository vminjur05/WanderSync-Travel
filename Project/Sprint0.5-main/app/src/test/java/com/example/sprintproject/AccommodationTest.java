//Jyotir Sompalli's Tests
package com.example.sprintproject;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.sprintproject.model.Accommodation;

public class AccommodationTest {

    private Accommodation accommodation;

    @Before
    public void setUp() {
        // Initializing a new accommodation instance with sample data
        accommodation = new Accommodation("12/01/2024", "12/05/2024", "Paris", "2", "Single");
    }

    @Test
    public void testConstructor() {
        // Testing constructor initialization
        assertEquals("12/01/2024", accommodation.getCheckInDate());
        assertEquals("12/05/2024", accommodation.getCheckOutDate());
        assertEquals("Paris", accommodation.getLocation());
        assertEquals("2", accommodation.getNumberOfRooms());
        assertEquals("Single", accommodation.getRoomType());
    }

    @Test
    public void testGettersAndSetters() {
        // Testing the getters and setters for all fields

        // Test the getter values
        assertEquals("12/01/2024", accommodation.getCheckInDate());
        assertEquals("12/05/2024", accommodation.getCheckOutDate());
        assertEquals("Paris", accommodation.getLocation());
        assertEquals("2", accommodation.getNumberOfRooms());
        assertEquals("Single", accommodation.getRoomType());

        // Set new values for the accommodation fields
        accommodation.setCheckInDate("12/10/2024");
        accommodation.setCheckOutDate("12/15/2024");
        accommodation.setLocation("London");
        accommodation.setNumberOfRooms("3");
        accommodation.setRoomType("Double");

        // Test the setter values
        assertEquals("12/10/2024", accommodation.getCheckInDate());
        assertEquals("12/15/2024", accommodation.getCheckOutDate());
        assertEquals("London", accommodation.getLocation());
        assertEquals("3", accommodation.getNumberOfRooms());
        assertEquals("Double", accommodation.getRoomType());
    }

    @Test
    public void testEmptyConstructor() {
        // Create a new accommodation instance using the empty constructor
        Accommodation emptyAccommodation = new Accommodation();

        // Assert that all values are initially null or empty
        assertNull(emptyAccommodation.getCheckInDate());
        assertNull(emptyAccommodation.getCheckOutDate());
        assertNull(emptyAccommodation.getLocation());
        assertNull(emptyAccommodation.getNumberOfRooms());
        assertNull(emptyAccommodation.getRoomType());
    }
}
