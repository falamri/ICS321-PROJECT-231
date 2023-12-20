package com.example.blooddonationapp;

public class BloodBagItem {
    private String name;
    private int bloodBagID;

    public BloodBagItem(String name, int bloodBagID) {
        this.name = name;
        this.bloodBagID = bloodBagID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBloodBagID() {
        return bloodBagID;
    }

    public void setBloodBagID(int bloodBagID) {
        this.bloodBagID = bloodBagID;
    }
}
