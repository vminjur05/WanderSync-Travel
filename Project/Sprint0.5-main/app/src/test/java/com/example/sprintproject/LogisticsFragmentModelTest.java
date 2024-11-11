//Jude Karaki and Vignesh Minjur's Tests
package com.example.sprintproject;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.sprintproject.model.LogisticsFragmentModel;

public class LogisticsFragmentModelTest {

    private LogisticsFragmentModel logistics;

    @Before
    public void setUp() {
        // Initializing a new LogisticsFragmentModel instance with sample data
        logistics = new LogisticsFragmentModel("Warehouse A", "01/01/2024 08:00", "01/01/2024 17:00");
    }

    @Test
    public void testConstructor() {
        // Testing constructor initialization
        assertEquals("Warehouse A", logistics.getLocation());
        assertEquals("01/01/2024 08:00", logistics.getEstimatedStart());
        assertEquals("01/01/2024 17:00", logistics.getEstimatedEnd());
    }

    @Test
    public void testGettersAndSetters() {
        // Test initial getter values
        assertEquals("Warehouse A", logistics.getLocation());
        assertEquals("01/01/2024 08:00", logistics.getEstimatedStart());
        assertEquals("01/01/2024 17:00", logistics.getEstimatedEnd());

        // Set new values
        logistics.setLocation("Warehouse B");
        logistics.setEstimatedStart("01/02/2024 09:00");
        logistics.setEstimatedEnd("01/02/2024 18:00");

        // Verify setter values
        assertEquals("Warehouse B", logistics.getLocation());
        assertEquals("01/02/2024 09:00", logistics.getEstimatedStart());
        assertEquals("01/02/2024 18:00", logistics.getEstimatedEnd());
    }

    @Test
    public void testEmptyConstructor() {
        // Create a new instance using the empty constructor
        LogisticsFragmentModel emptyLogistics = new LogisticsFragmentModel();

        // Check that fields are initially null
        assertNull(emptyLogistics.getLocation());
        assertNull(emptyLogistics.getEstimatedStart());
        assertNull(emptyLogistics.getEstimatedEnd());
    }

    @Test
    public void testToString() {
        // Verify the string output of toString method
        String expectedString = "Location: Warehouse A\nStart: 01/01/2024 08:00\nEnd: 01/01/2024 17:00";
        assertEquals(expectedString, logistics.toString());
    }
}
