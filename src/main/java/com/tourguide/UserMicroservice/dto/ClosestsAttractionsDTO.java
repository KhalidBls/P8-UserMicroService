package com.tourguide.UserMicroservice.dto;


import java.util.ArrayList;
import java.util.List;

public class ClosestsAttractionsDTO {

    List<AttractionDTO> attractionDTOList;
    PositionDTO userPosition;

    public ClosestsAttractionsDTO(){
        attractionDTOList = new ArrayList<>();
    }

    public List<AttractionDTO> getAttractionDTOList() {
        return attractionDTOList;
    }

    public void setAttractionDTOList(List<AttractionDTO> attractionDTOList) {
        this.attractionDTOList = attractionDTOList;
    }

    public PositionDTO getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(PositionDTO userPosition) {
        this.userPosition = userPosition;
    }
}
