package com.example.blooddonationapp;

public class BloodTypeItem {
    private int id,count;

    public BloodTypeItem(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        switch (this.id){
            case 1: return "O+";
            case 2 :return "O-";
            case 3 : return "A+";
            case 4 : return "A-";
            case 5 : return "B+";
            case 6 : return "B-";
            case 7 : return "AB+";
            case 8 : return "AB-";
            default: return "Nothing";
        }

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
