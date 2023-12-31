package com.example.blooddonationapp;

public class Donor extends Person{
        private String donationHistory;
        private String medicalHistory;

        public Donor(int id, String password, String email, String fname,
                     String lname, String dob, String num, String address,  int weight,String bloodType, String
                             donationHistory, String medicalHistory) {
            super(id, password, email,fname,lname,dob,num,address,weight,bloodType);
            this.donationHistory = donationHistory;
            this.medicalHistory = medicalHistory;
        }

        // Getters
        public String getDonationHistory() {
            return donationHistory;
        }

        public String getMedicalHistory() {
            return medicalHistory;
        }

        // Setters
        public void setDonationHistory(String donationHistory) {
            this.donationHistory = donationHistory;
        }

        public void setMedicalHistory(String medicalHistory) {
            this.medicalHistory = medicalHistory;
        }

        // Other methods
        public void donateBlood() {
            // Add code to donate blood
        }

        public void viewDonationHistory() {
            // Add code to view donation history
        }

        public void updateMedicalHistory() {
            // Add code to update medical history
        }
    }

