package com.example.blooddonationapp;

public class Drive {
    private String start_data,location;
    private int id;

    public Drive(String start_data, String location, int id) {
        this.start_data = start_data;
        this.location = location;
        this.id = id;
    }

    public String getStart_data() {
        return start_data;
    }

    public void setStart_data(String start_data) {
        this.start_data = start_data;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
