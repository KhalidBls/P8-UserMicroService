package com.tourguide.UserMicroservice.dto;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationDTO {
    public  UUID userId;
    public  PositionDTO location;
    public  Date timeVisited;

    public VisitedLocationDTO(){}

    public VisitedLocationDTO(UUID userId, PositionDTO location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }
}
