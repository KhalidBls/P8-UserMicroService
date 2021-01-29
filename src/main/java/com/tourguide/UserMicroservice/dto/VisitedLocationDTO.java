package com.tourguide.UserMicroservice.dto;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationDTO {
    public final UUID userId;
    public final PositionDTO location;
    public final Date timeVisited;

    public VisitedLocationDTO(UUID userId, PositionDTO location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }
}
