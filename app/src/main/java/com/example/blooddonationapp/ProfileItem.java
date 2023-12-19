package com.example.blooddonationapp;

public class ProfileItem {
    private Person person;
    private  int donationNum,receiveNum;

    public ProfileItem(Person person, int donationNum, int receiveNum) {
        this.person = person;
        this.donationNum = donationNum;
        this.receiveNum = receiveNum;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getDonationNum() {
        return donationNum;
    }

    public void setDonationNum(int donationNum) {
        this.donationNum = donationNum;
    }

    public int getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(int receiveNum) {
        this.receiveNum = receiveNum;
    }
}
