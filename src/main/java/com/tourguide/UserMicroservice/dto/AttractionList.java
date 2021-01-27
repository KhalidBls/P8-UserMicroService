package com.tourguide.UserMicroservice.dto;

import gpsUtil.location.Attraction;

import java.util.ArrayList;
import java.util.List;

public class AttractionList {

    private List<Attraction> attractionList;

    public AttractionList(){
        attractionList = new ArrayList<>();
    }

    public List<Attraction> getAttractionList() {
        return attractionList;
    }

    public void setAttractionList(List<Attraction> attractionList) {
        this.attractionList = attractionList;
    }
}
