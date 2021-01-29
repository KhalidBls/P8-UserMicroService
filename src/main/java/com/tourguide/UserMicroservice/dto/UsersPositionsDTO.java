package com.tourguide.UserMicroservice.dto;


import java.util.UUID;

public class UsersPositionsDTO {

    private UUID userId;
    private PositionDTO positionDTO;

    public UsersPositionsDTO(){}

    public UsersPositionsDTO(UUID userId, PositionDTO positionDTO) {
        this.userId = userId;
        this.positionDTO = positionDTO;
    }

    public UsersPositionsDTO(UUID userId, VisitedLocationDTO visitedLocation) {
        this.userId = userId;
        this.positionDTO = new PositionDTO(visitedLocation.location.getLatitude(),visitedLocation.location.getLongitude());
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public PositionDTO getPositionDTO() {
        return positionDTO;
    }

    public void setPositionDTO(PositionDTO positionDTO) {
        this.positionDTO = positionDTO;
    }
}
