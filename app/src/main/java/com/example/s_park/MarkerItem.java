package com.example.s_park;

public class MarkerItem {
    double lat;
    double lon;
    String state;
    String numOfpark;
    String userOfpark;
    String phoneOfpark;


    public MarkerItem(double lat, double lon, String state, String numOfpark, String phoneOfpark, String userOfpark){
        this.lat = lat;
        this.lon = lon;
        this.state = state;
        this.numOfpark = numOfpark;
        this.userOfpark = userOfpark;
        this.phoneOfpark = phoneOfpark;
    }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getNumOfpark() {return numOfpark;}
    public void setNumOfpark(String numOfpark){ this.numOfpark = numOfpark;}

    public String getNameOfpark() {return userOfpark;}

    public void setNameOfpark(String userOfpark) {this.userOfpark = userOfpark; }

    public String getPhoneOfpark() {return phoneOfpark;}

    public void setPhoneOfpark(String phoneOfpark) {this.phoneOfpark = phoneOfpark;}
}

