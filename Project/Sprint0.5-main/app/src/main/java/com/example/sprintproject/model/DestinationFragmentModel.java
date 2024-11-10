package com.example.sprintproject.model;

public class DestinationFragmentModel {
    private String location;
    private String estimatedStart;
    private String estimatedEnd;

    // Default constructor required for calls to
    // DataSnapshot.getValue(DestinationFragmentModel.class)
    public DestinationFragmentModel() { }

    public DestinationFragmentModel(String location, String estimatedStart, String estimatedEnd) {
        this.location = location;
        this.estimatedStart = estimatedStart;
        this.estimatedEnd = estimatedEnd; //lawl
    }

    // Getters and setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEstimatedStart() {
        return estimatedStart;
    }

    public void setEstimatedStart(String estimatedStart) {
        this.estimatedStart = estimatedStart;
    }

    public String getEstimatedEnd() {
        return estimatedEnd;
    }

    public void setEstimatedEnd(String estimatedEnd) {
        this.estimatedEnd = estimatedEnd;
    }

    @Override
    public String toString() {
        return "Location: " + location + "\nStart: " + estimatedStart + "\nEnd: " + estimatedEnd;
    }
}
