package com.example.sprintproject.model;

public class DestinationFragmentModel {
    private String location;
    private String estimatedStart;
    private String estimatedEnd;

    public DestinationFragmentModel(String location, String estimatedStart, String estimatedEnd) {
        this.location = location;
        this.estimatedStart = estimatedStart;
        this.estimatedEnd = estimatedEnd;
    }

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
}