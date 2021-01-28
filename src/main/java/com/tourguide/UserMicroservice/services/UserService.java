package com.tourguide.UserMicroservice.services;

import com.tourguide.UserMicroservice.dto.ClosestsAttractionsDTO;
import com.tourguide.UserMicroservice.dto.User;
import com.tourguide.UserMicroservice.helper.InternalTestHelper;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    ConsumerService consumerService;

    public UserService(){
        consumerService = new ConsumerService();
        initializeInternalUsers();
    }

    public ClosestsAttractionsDTO getClosestAttractionsDTO(String userName) {
        Location userLocation = getUserLocation(getUser(userName)).location;
        ClosestsAttractionsDTO closestsAttractionsDTO = new ClosestsAttractionsDTO();

        closestsAttractionsDTO.setAttractionDTOList(consumerService.getAttractions().stream()
                .sorted(Comparator.comparingDouble(a -> getDistance(userLocation, new Location(a.getLatitude(), a.getLongitude()))))
                .limit(5).collect(Collectors.toList()));
        closestsAttractionsDTO.getAttractionDTOList().parallelStream().forEach(attractionDTO -> attractionDTO.setDistance(getDistance(userLocation, new Location(attractionDTO.getLatitude(), attractionDTO.getLongitude()))));
        closestsAttractionsDTO.setUserPosition(consumerService.getUserLocation(getUser(userName)));

        return closestsAttractionsDTO;
    }

    public VisitedLocation getUserLocation(User user) {
        return user.getVisitedLocations().get(user.getVisitedLocations().size()-1);
    }

    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    private double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;

    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/

    private final Map<String, User> internalUserMap = new HashMap<>();

    public void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i-> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }


}
