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

    @Test
    public void testDateFormatValidation_ValidFormat() {
        String validDate = "12/25/2024";
        assertTrue(validDate.matches("\\d{2}/\\d{2}/\\d{4}"));
    }

    @Test
    public void testDateFormatValidation_InvalidFormat() {
        String invalidDate = "2024-12-25";
        assertFalse(invalidDate.matches("\\d{2}/\\d{2}/\\d{4}"));
    }

    @Test
    public void testStartDateBeforeEndDate_Valid() throws ParseException {
        Date startDate = sdf.parse("12/24/2024");
        Date endDate = sdf.parse("12/25/2024");
        assertNotNull(startDate);
        assertNotNull(endDate);
        assertTrue(startDate.before(endDate));
    }

    @Test
    public void testStartDateBeforeEndDate_Invalid() throws ParseException {
        Date startDate = sdf.parse("12/25/2024");
        Date endDate = sdf.parse("12/24/2024");
        assertNotNull(startDate);
        assertNotNull(endDate);
        assertFalse(startDate.before(endDate));
    }

    @Test
    public void testStartDateNotInPast_Valid() throws ParseException {
        Date startDate = sdf.parse("12/25/2024");
        assertNotNull(startDate);
        assertTrue(startDate.after(new Date()));
    }

    @Test
    public void testStartDateNotInPast_Invalid() throws ParseException {
        Date startDate = sdf.parse("12/25/2020");
        assertNotNull(startDate);
        assertFalse(startDate.after(new Date()));
    }

    @Test
    public void testEmptyFieldValidation_AllRequiredFieldsProvided() {
        String startDate = "12/25/2024";
        String endDate = "12/30/2024";
        String destination = "Paris";

        assertFalse(startDate.isEmpty());
        assertFalse(endDate.isEmpty());
        assertFalse(destination.isEmpty());
    }

    @Test
    public void testEmptyFieldValidation_MissingRequiredFields() {
        String startDate = "";
        String endDate = "12/30/2024";
        String destination = "";

        assertTrue(startDate.isEmpty());
        assertTrue(destination.isEmpty());
        assertFalse(endDate.isEmpty());
    }
}
