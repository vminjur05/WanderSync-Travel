package com.example.sprintproject.model;

public class Accommodation {
    private String checkInDate;
    private String checkOutDate;
    private String location;
    private String numberOfRooms;
    private String roomType;

    // Empty constructor required for Firebase deserialization
    public Accommodation() { }

    public Accommodation(String checkInDate, String checkOutDate,
                         String location, String numberOfRooms, String roomType) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.location = location;
        this.numberOfRooms = numberOfRooms;
        this.roomType = roomType;
    }

    // Getters and setters
    public String getCheckInDate() {
        return checkInDate;
    }
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }
    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}
