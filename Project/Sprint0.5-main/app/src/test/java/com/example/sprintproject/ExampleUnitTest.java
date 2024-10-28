package com.example.sprintproject;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.sprintproject.model.DestinationFragmentModel;

/**
 * Unit test for DestinationFragmentModel class.
 */
public class ExampleUnitTest {

    @Test
    public void testDestinationFragmentModel() {
        // Create a new instance with test data
        DestinationFragmentModel model = new DestinationFragmentModel("New York", "2024-12-01", "2024-12-10");

        // Verify the location
        assertEquals("New York", model.getLocation());

        // Verify the start date
        assertEquals("2024-12-01", model.getEstimatedStart());

        // Verify the end date
        assertEquals("2024-12-10", model.getEstimatedEnd());

        // Test toString method
        String expectedString = "Location: New York\nStart: 2024-12-01\nEnd: 2024-12-10";
        assertEquals(expectedString, model.toString());
    }
}
