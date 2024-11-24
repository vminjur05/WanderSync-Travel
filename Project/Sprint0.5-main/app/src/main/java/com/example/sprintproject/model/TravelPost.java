package com.example.sprintproject.model;

public class TravelPost {
    public String id, startDate, endDate, destination, accommodations, dining, notes;

    public TravelPost() {
        // Default constructor required for Firebase
    }
    // Constructor to initialize the TravelPost object
    public TravelPost(String id, String startDate, String endDate, String destination, String accommodations, String dining, String notes) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.accommodations = accommodations;
        this.dining = dining;
        this.notes = notes;
    }
}
