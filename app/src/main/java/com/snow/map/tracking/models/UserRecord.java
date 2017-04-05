package com.snow.map.tracking.models;

/**
 * Created by Muhammad Shan on 05/03/2017.
 */

public class UserRecord {
    public String time;
    public String date;
    public double miles;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRecord() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }

    public UserRecord(String time, String date, double miles, String email) {
        this.time = time;
        this.date = date;
        this.miles = miles;
        this.email = email;
    }
}
