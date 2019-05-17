package com.example.calorietracker;

public class Park {

    Double latitude;
    Double longitude;
    String name;


    public Park(Double lat, Double lng, String park)
    {
        latitude = lat;
        longitude = lng;
        name = park;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setLatitude(Double lat)
    {
        this.latitude = lat;
    }

    public void setLongitude(Double lng)
    {
        this.longitude = lng;
    }

    public void setName(String park)
    {
        this.name = park;
    }
}
