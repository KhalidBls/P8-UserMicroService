package com.tourguide.UserMicroservice.dto;

import java.util.UUID;

public class AttractionDTO {
    public  String attractionName;
    public  String city;
    public  String state;
    public  UUID attractionId;
    private double longitude;
    private double latitude;
    private double distance;
    private Integer rewards;

    public AttractionDTO(){ }

    public AttractionDTO(String attractionName, String city, String state, double latitude, double longitude) {
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Integer getRewards() {
        return rewards;
    }

    public void setRewards(Integer rewards) {
        this.rewards = rewards;
    }
}
