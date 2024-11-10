package com.example.sprintproject.model;

public class Reservation {
    private String id; // Unique identifier for each reservation
    private String location;
    private String website;
    private String reservationTime;
    private int rating; // Field for star rating

    // Empty constructor needed for Firebase
    public Reservation() { }

    public Reservation(String id, String location, String website, String reservationTime) {
        this.id = id;
        this.location = location;
        this.website = website;
        this.reservationTime = reservationTime;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public int getRating() {
        return rating;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
