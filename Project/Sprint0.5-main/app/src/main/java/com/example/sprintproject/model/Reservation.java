package com.example.sprintproject.model;

public class Reservation {
    private static Reservation instance; // Static instance to hold the singleton
    private String id; // Unique identifier for each reservation
    private String location;
    private String website;
    private String reservationTime;
    private int rating; // Field for star rating

    // Private constructor to prevent instantiation
    // Empty constructor needed for Firebase
    public Reservation() { }

    public Reservation(String id, String location, String website, String reservationTime) {
        this.id = id;
        this.location = location;
        this.website = website;
        this.reservationTime = reservationTime;
    }
    // Public static method to provide access to the single instance
    public static synchronized Reservation getInstance() {
        if (instance == null) {
            instance = new Reservation();
        }
        return instance;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
