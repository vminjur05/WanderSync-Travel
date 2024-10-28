package com.example.sprintproject;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.sprintproject.model.DestinationFragmentModel;

/**
 * Unit tests for DestinationFragmentModel class.
 */
public class DestinationModelFragmentTest {

    @Test //Jyotir Sompalli
    public void testDefaultConstructor() {
        DestinationFragmentModel model = new DestinationFragmentModel();
        assertNull(model.getLocation());
        assertNull(model.getEstimatedStart());
        assertNull(model.getEstimatedEnd());
    }

    @Test //Jyotir Sompalli
    public void testSetLocation() {
        DestinationFragmentModel model = new DestinationFragmentModel();
        model.setLocation("Paris");
        assertEquals("Paris", model.getLocation());
    }

    @Test //Andrew Nguyen
    public void testSetEstimatedStart() {
        DestinationFragmentModel model = new DestinationFragmentModel();
        model.setEstimatedStart("2024-11-15");
        assertEquals("2024-11-15", model.getEstimatedStart());
    }

    @Test //Andrew Nguyen
    public void testSetEstimatedEnd() {
        DestinationFragmentModel model = new DestinationFragmentModel();
        model.setEstimatedEnd("2024-11-25");
        assertEquals("2024-11-25", model.getEstimatedEnd());
    }

    @Test //Abhiram Chilakamarri
    public void testToStringWithEmptyFields() {
        DestinationFragmentModel model = new DestinationFragmentModel();
        String expected = "Location: null\nStart: null\nEnd: null";
        assertEquals(expected, model.toString());
    }

    @Test //Abhiram Chilakamarri
    public void testToStringWithPartialData() {
        DestinationFragmentModel model = new DestinationFragmentModel("Berlin", null, "2024-12-05");
        String expected = "Location: Berlin\nStart: null\nEnd: 2024-12-05";
        assertEquals(expected, model.toString());
    }

    @Test //Vignesh Minjur
    public void testToStringWithSpecialCharacters() {
        DestinationFragmentModel model = new DestinationFragmentModel("Tokyo!", "2024-@1-01", "2024-@1-05");
        String expected = "Location: Tokyo!\nStart: 2024-@1-01\nEnd: 2024-@1-05";
        assertEquals(expected, model.toString());
    }

    @Test //Vignesh Minjur
    public void testLocationGetterSetter() {
        DestinationFragmentModel model = new DestinationFragmentModel();
        model.setLocation("London");
        assertEquals("London", model.getLocation());
        model.setLocation("Amsterdam");
        assertEquals("Amsterdam", model.getLocation());
    }

    @Test //Jude Karaki
    public void testEstimatedStartGetterSetter() {
        DestinationFragmentModel model = new DestinationFragmentModel();
        model.setEstimatedStart("2024-10-20");
        assertEquals("2024-10-20", model.getEstimatedStart());
        model.setEstimatedStart("2024-11-01");
        assertEquals("2024-11-01", model.getEstimatedStart());
    }

    @Test //Jude Karaki
    public void testEstimatedEndGetterSetter() {
        DestinationFragmentModel model = new DestinationFragmentModel();
        model.setEstimatedEnd("2025-01-10");
        assertEquals("2025-01-10", model.getEstimatedEnd());
        model.setEstimatedEnd("2025-02-15");
        assertEquals("2025-02-15", model.getEstimatedEnd());
    }
}
