//Abhiram's Tests
package com.example.sprintproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.sprintproject.model.MainFragmentModel;

import org.junit.Before;
import org.junit.Test;

public class MainFragmentModelTest {

    private MainFragmentModel mainFragmentModel;

    @Before
    public void setUp() {
        mainFragmentModel = new MainFragmentModel();
    }

    @Test
    public void testInitialCurrentPageIsEmpty() {
        // Check if currentPage is initialized as an empty string
        assertNotNull("currentPage should not be null on initialization", mainFragmentModel.getCurrentPage());
        assertEquals("Initial currentPage should be an empty string", "", mainFragmentModel.getCurrentPage());
    }

    @Test
    public void testSetCurrentPageDoesNotChangeAnything() {
        // Calling setCurrentPage() and verifying it does not alter the currentPage value
        String initialPage = mainFragmentModel.getCurrentPage();
        mainFragmentModel.setCurrentPage();  // Since setCurrentPage has no effect in the existing code
        assertEquals("Calling setCurrentPage() should not change currentPage", initialPage, mainFragmentModel.getCurrentPage());
    }
}
