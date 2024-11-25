//Since this is the only major change for sprint 4, this is all 8 Junit tests.
package com.example.sprintproject;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class TravelCommunityFragmentTest {

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    @Test //Jyotir Sompalli's Tests
    public void testDateFormatValidation_ValidFormat() {
        String validDate = "12/25/2024";
        assertTrue(validDate.matches("\\d{2}/\\d{2}/\\d{4}"));
    }

    @Test //Jyotir Sompalli's Tests
    public void testDateFormatValidation_InvalidFormat() {
        String invalidDate = "2024-12-25";
        assertFalse(invalidDate.matches("\\d{2}/\\d{2}/\\d{4}"));
    }

    @Test //Andrew Nguyen's Tests
    public void testStartDateBeforeEndDate_Valid() throws ParseException {
        Date startDate = sdf.parse("12/24/2024");
        Date endDate = sdf.parse("12/25/2024");
        assertNotNull(startDate);
        assertNotNull(endDate);
        assertTrue(startDate.before(endDate));
    }

    @Test //Andrew Nguyen's Tests
    public void testStartDateBeforeEndDate_Invalid() throws ParseException {
        Date startDate = sdf.parse("12/25/2024");
        Date endDate = sdf.parse("12/24/2024");
        assertNotNull(startDate);
        assertNotNull(endDate);
        assertFalse(startDate.before(endDate));
    }

    @Test //Jude Karaki's Tests
    public void testStartDateNotInPast_Valid() throws ParseException {
        Date startDate = sdf.parse("12/25/2024");
        assertNotNull(startDate);
        assertTrue(startDate.after(new Date()));
    }

    @Test //Jude Karaki's Tests
    public void testStartDateNotInPast_Invalid() throws ParseException {
        Date startDate = sdf.parse("12/25/2020");
        assertNotNull(startDate);
        assertFalse(startDate.after(new Date()));
    }

    @Test //Abhiram Chilakamarri's Tests
    public void testEmptyFieldValidation_AllRequiredFieldsProvided() {
        String startDate = "12/25/2024";
        String endDate = "12/30/2024";
        String destination = "Paris";

        assertFalse(startDate.isEmpty());
        assertFalse(endDate.isEmpty());
        assertFalse(destination.isEmpty());
    }

    @Test //Abhiram Chilakamarri's Tests
    public void testEmptyFieldValidation_MissingRequiredFields() {
        String startDate = "";
        String endDate = "12/30/2024";
        String destination = "";

        assertTrue(startDate.isEmpty());
        assertTrue(destination.isEmpty());
        assertFalse(endDate.isEmpty());
    }

    @Test //Vignesh Minjur's Tests
    public void testEndDateNotBeforeStartDate() throws ParseException {
        Date startDate = sdf.parse("12/25/2024");
        Date endDate = sdf.parse("12/24/2024");
        assertNotNull(startDate);
        assertNotNull(endDate);
        assertFalse(endDate.after(startDate));
    }

    @Test //Vignesh Minjur's Tests
    public void testAllFieldsCombinedValidation() throws ParseException {
        String startDate = "12/25/2024";
        String endDate = "12/30/2024";
        String destination = "London";
        String accommodations = "Hotel";
        String dining = "Local cuisine";
        String notes = "Pack warm clothes";

        // Ensure required fields are not empty
        assertFalse(startDate.isEmpty());
        assertFalse(endDate.isEmpty());
        assertFalse(destination.isEmpty());

        // Validate date format
        assertTrue(startDate.matches("\\d{2}/\\d{2}/\\d{4}"));
        assertTrue(endDate.matches("\\d{2}/\\d{2}/\\d{4}"));

        // Validate date logic
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        assertNotNull(start);
        assertNotNull(end);
        assertTrue(start.before(end));
        assertTrue(start.after(new Date())); // Ensure start date is not in the past

        // Check optional fields
        assertFalse(accommodations.isEmpty());
        assertFalse(dining.isEmpty());
        assertNotNull(notes);
    }
}
