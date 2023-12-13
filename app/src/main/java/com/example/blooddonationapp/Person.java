package com.example.blooddonationapp;


public class Person {
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
    private String Weight;

    public Person(int id, String username, String password, String email, String fname,
                  String lname, String dob, String num, String address, String healthStatus, String weight) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.num = num;
        this.address = address;
        this.healthStatus = healthStatus;
        Weight = weight;
    }

    // Getters
    public int getId() {
        return id;
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

    public String getWeight() {
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

    public void setWeight(String weight) {
        Weight = weight;
    }
}
