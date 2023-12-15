package com.example.blooddonationapp;

public class Recipient extends Person {
    private String recipientHistory;
    private String medicalHistory;

    public Recipient(int id, String password, String email, String fname,
                 String lname, String dob, String num, String address, int weight,String bloodType,
                     String recipientHistory, String medicalHistory) {
        super(id, password, email,fname,lname,dob,num,address,weight,bloodType);
        this.recipientHistory = recipientHistory;
        this.medicalHistory = medicalHistory;
    }

    // Getters
    public String getRecipientHistory() {
        return recipientHistory;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    // Setters
    public void setRecipientHistory(String recipientHistory) {
        this.recipientHistory = recipientHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    // Other methods
    public void receiveBlood() {
        // Add code to receive blood
    }

    public void viewRecipientHistory() {
        // Add code to view recipient history
    }

    public void updateMedicalHistory() {
        // Add code to update medical history
    }

    public void makePayment() {
        // Add code to make payment
    }
}

