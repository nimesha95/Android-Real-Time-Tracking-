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
    private String custname;
    private String address;
    private String itemCatagories;
    private Long contactNo;
    private Long timestamp;

    public JobClass(String key, Double lat, Double longi, String custname, String address, String itemCatagories, Long contactNo, Long timestamp) {
        this.key = key;
        this.lat = lat;
        this.longi = longi;
        this.custname = custname;
        this.address = address;
        this.itemCatagories = itemCatagories;
        this.contactNo = contactNo;
        this.timestamp = timestamp;
    }

    JobClass(String key, Double lat, Double longi) {
        this.key = key;
        this.lat = lat;
        this.longi = longi;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getItemCatagories() {
        return itemCatagories;
    }

    public void setItemCatagories(String itemCatagories) {
        this.itemCatagories = itemCatagories;
    }

    public Long getContactNo() {
        return contactNo;
    }

    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}