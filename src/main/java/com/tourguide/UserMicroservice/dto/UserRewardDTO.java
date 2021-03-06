package com.tourguide.UserMicroservice.dto;


public class UserRewardDTO {


        public final VisitedLocationDTO visitedLocation;
        public final AttractionDTO attraction;
        private int rewardPoints;

        public UserRewardDTO(VisitedLocationDTO visitedLocation, AttractionDTO attraction, int rewardPoints) {
            this.visitedLocation = visitedLocation;
            this.attraction = attraction;
            this.rewardPoints = rewardPoints;
        }

        public UserRewardDTO(VisitedLocationDTO visitedLocation, AttractionDTO attraction) {
            this.visitedLocation = visitedLocation;
            this.attraction = attraction;
        }

        public void setRewardPoints(int rewardPoints) {
            this.rewardPoints = rewardPoints;
        }

        public int getRewardPoints() {
            return rewardPoints;
        }

}
