package com.example.nimesha.delivery;

/**
 * Created by Nimesha on 8/26/2017.
 * This class is used to create object of the current delivery jobs
 * Mainly used to handle the datasnapshots that get from Firebase realtime database
 */

public class JobClass {
    private String key;
    private Double lat;
    private Double longi;

    JobClass(String key, Double lat, Double longi) {
        this.key = key;
        this.lat = lat;
        this.longi = longi;
    }

    public String getKey() {
        return key;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLongi() {
        return longi;
    }
}
