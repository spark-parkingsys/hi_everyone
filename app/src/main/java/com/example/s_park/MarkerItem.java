package com.example.s_park;

public class MarkerItem {
    double lat;
    double lon;
    boolean state;
    String numOfpark;

    public MarkerItem(double lat, double lon, boolean state, String numOfpark){
        this.lat = lat;
        this.lon = lon;
        this.state = state;
        this.numOfpark = numOfpark;
    }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public boolean getState() { return state; }
    public void setState(boolean state) { this.state = state; }

    public String getNumOfpark() {return numOfpark;}
    public void setNumOfpark(String numOfpark){ this.numOfpark = numOfpark;}
}

