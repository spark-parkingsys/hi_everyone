package com.example.s_park;

public class MarkerItem {
    double lat;
    double lon;
    String state;
    String numOfpark;
    String userOfpark;
    String phoneOfpark;

    /*"latlng":{"lat":37.59301166,"lng":127.0892316},
            "_id":"5f3d1ab6d65be674083a2493",
            "oID":"2015920001",
            "pID":"123126020010103",
            "placeName":"면목본동4",
            "addrName":"서울 중랑구 면목동 284",
            "hasSensor":false,
            "status":null,
            "ownerID":null,
            "createdAt":"2020-08-19T12:27:34.854Z",
            "updatedAt":"2020-08-19T12:27:34.854Z",
            "__v":0*/


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

