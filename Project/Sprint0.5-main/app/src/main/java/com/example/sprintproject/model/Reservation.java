package com.example.sprintproject.model;

public class Reservation {
    private String location;
    private String website;
    private String reservationTime;

    // Empty constructor needed for Firebase
    public Reservation() { }

    public Reservation(String location, String website, String reservationTime) {
        this.location = location;
        this.website = website;
        this.reservationTime = reservationTime;
    }

    // Getters
    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getReservationTime() {
        return reservationTime;
    }

}
