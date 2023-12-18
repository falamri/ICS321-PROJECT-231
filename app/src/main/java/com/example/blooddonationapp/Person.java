package com.example.blooddonationapp;


import java.io.Serializable;

public class Person implements Serializable {
    private int id;
    private String username;
    private String password;
    private String email;
    private String fname;
    private String lname;
    private String dob;
    private String num;
    private String address;
    private String healthStatus;
    private int Weight;
    private String bloodType;

    public Person(int id, String password, String email, String fname,
                  String lname, String dob, String num, String address, int weight,String bloodType) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.num = num;
        this.address = address;
        Weight = weight;
        this.bloodType=bloodType;
    }

    public String getBloodType() {
        return bloodType;
    }

    public  String getType() {
        if (bloodType == null || bloodType.isEmpty()) {
            return "Unknown";
        }

        bloodType = bloodType.toUpperCase();

        switch (bloodType) {
            case "A+":
            case "A-":
                return "A";
            case "B+":
            case "B-":
                return "B";
            case "AB+":
            case "AB-":
                return "AB";
            case "O+":
            case "O-":
                return "O";
            default:
                return "Unknown";
        }
    }
    public int getBloodId(String type){
        switch(type){
            case "Positive O": return 1;
            case "Negative O": return 2;
            case "Positive A":return 3;
            case "Negative A":return 4;
            case "Positive B":return 5;
            case"Negative B":return 6;
            case "Positive AB": return 7;
            case "Negative AB":return 8;
            default: return -1;
        }
    }

    public  String getSign() {
        if (bloodType == null || bloodType.isEmpty()) {
            return "Unknown";
        }

        bloodType = bloodType.toUpperCase();

        switch (bloodType) {
            case "A+":
            case "O+":
            case "A-":
            case "O-":
                return "Positive";
            case "B+":
            case "AB+":
            case "B-":
            case "AB-":
                return "Negative";
            default:
                return "Unknown";
        }
    }
    // Getters
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", dob='" + dob + '\'' +
                ", num='" + num + '\'' +
                ", address='" + address + '\'' +
                ", healthStatus='" + healthStatus + '\'' +
                ", Weight=" + Weight +
                ", bloodType='" + bloodType + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return fname+" "+lname;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getDob() {
        return dob;
    }

    public String getNum() {
        return num;
    }

    public String getAddress() {
        return address;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public int getWeight() {
        return Weight;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }
}
