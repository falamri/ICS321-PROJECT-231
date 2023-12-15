package com.example.blooddonationapp;

public class BloodBag {
    private String blood_type,expDate,collectionDate;
    private int donorId,id;

    public BloodBag(String blood_type, String expDate, String collectionDate, int donorId, int id) {
        this.blood_type = blood_type;
        this.expDate = expDate;
        this.collectionDate = collectionDate;
        this.donorId = donorId;
        this.id = id;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
