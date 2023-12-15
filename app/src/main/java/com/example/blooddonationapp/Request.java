package com.example.blooddonationapp;

public class Request {
    private int id,personID;
    private String fname,status,bloodType;

    public Request(int id, int personID, String fname, String status, String bloodType) {
        this.id = id;
        this.personID = personID;
        this.fname = fname;
        this.status = status;
        this.bloodType = bloodType;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
