package com.example.blooddonationapp;

public class DonorItem {
    private String fname,lname, date;

    public DonorItem(String fname, String lname, String date) {
        this.fname = fname;
        this.lname = lname;
        this.date = date;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
