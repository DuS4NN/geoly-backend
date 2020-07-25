package com.geoly.app.models;

public class Coordinates {

    private Double latitude;
    private Double longitude;

    public Coordinates(){
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCoordinates(){
        return latitude + ";" + longitude;
    }
}
