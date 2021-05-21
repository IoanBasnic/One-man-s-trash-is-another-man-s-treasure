package com.example.demo.DataModels.client;

public class Coordinates{
    public Coordinates(Float lat, Float lng){
        this.latitude = lat;
        this.longitude = lng;
    }

    Float latitude;
    Float longitude;

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}