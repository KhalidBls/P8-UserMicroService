package com.tourguide.UserMicroservice.services;

import com.tourguide.UserMicroservice.dto.*;
import com.tourguide.UserMicroservice.helper.InternalTestHelper;
import com.tourguide.UserMicroservice.proxies.ProxyGps;
import com.tourguide.UserMicroservice.proxies.ProxyRewards;
import com.tourguide.UserMicroservice.trackers.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final TripPricer tripPricer = new TripPricer();
    private Tracker tracker;
    private ProxyGps proxyGps;
    private ProxyRewards proxyRewards;


    public UserService(){
        tracker = new Tracker(this);
        proxyGps = new ProxyGps();
        proxyRewards = new ProxyRewards();
        initializeInternalUsers();
        addShutDownHook();
    }

    public Tracker getTracker() {
        return tracker;
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }

    public ClosestsAttractionsDTO getClosestAttractionsDTO(User user) {
        PositionDTO userLocation = getUserLocation(user).location;
        ClosestsAttractionsDTO closestsAttractionsDTO = new ClosestsAttractionsDTO();

        closestsAttractionsDTO.setAttractionDTOList(proxyGps.getAttractions().stream()
                .sorted(Comparator.comparingDouble(a -> getDistance(userLocation, new PositionDTO(a.getLatitude(), a.getLongitude()))))
                .limit(5).collect(Collectors.toList()));
        closestsAttractionsDTO.getAttractionDTOList().parallelStream().forEach(attractionDTO -> {
            attractionDTO.setDistance(getDistance(userLocation, new PositionDTO(attractionDTO.getLatitude(), attractionDTO.getLongitude())));
            attractionDTO.setRewards(proxyRewards.getRewardPoints(attractionDTO.getAttractionId(),user.getUserId()));
        });

        closestsAttractionsDTO.setUserPosition(new PositionDTO(userLocation.getLatitude(),userLocation.getLongitude()));

        return closestsAttractionsDTO;
    }

    public VisitedLocationDTO getUserLocation(User user) {
        if (!user.getVisitedLocations().isEmpty())
            return user.getVisitedLocations().get(user.getVisitedLocations().size()-1);
        else{
            VisitedLocationDTO visitedLocationDTO = proxyGps.getUserLocation(user.getUserId());
            PositionDTO currentPosition = new PositionDTO(visitedLocationDTO.location.getLatitude(),visitedLocationDTO.location.getLongitude());
            return new VisitedLocationDTO(user.getUserId(),new PositionDTO(currentPosition.getLatitude(),currentPosition.getLongitude()),new Date());
        }
    }

    public void calculateRewards(User user) {
        List<VisitedLocationDTO> userLocations = user.getVisitedLocations();
        List<AttractionDTO> attractions = proxyGps.getAttractions();

        for(VisitedLocationDTO visitedLocation : userLocations) {
            for(AttractionDTO attraction : attractions) {
                if(user.getUserRewards().stream().filter(rewardDTO -> rewardDTO.attraction.getAttractionName().equals(attraction.getAttractionName())).count() == 0) {
                    if(nearAttraction(visitedLocation, attraction)) {
                        user.addUserReward(new UserRewardDTO(visitedLocation, attraction, proxyRewards.getRewardPoints(attraction.getAttractionId(), user.getUserId())));
                    }
                }
            }
        }
    }

    public void trackUserLocation(User user){
        user.addToVisitedLocations(proxyGps.getUserLocation(user.getUserId()));
        calculateRewards(user);
    }

    private boolean nearAttraction(VisitedLocationDTO visitedLocation, AttractionDTO attraction) {
        final int proximityBuffer = 9999; //10 a la base
        return getDistance( new PositionDTO(attraction.getLatitude(), attraction.getLongitude()),visitedLocation.location) < proximityBuffer ;
    }

    public List<Provider> getTripDeals(User user){
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    public List<UserRewardDTO> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public List<User> getAllUsers() {
        return internalUserMap.values().stream().collect(Collectors.toList());
    }

    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    private double getDistance(PositionDTO loc1, PositionDTO loc2) {
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lon1 = Math.toRadians(loc1.getLongitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }

    public List<UsersPositionsDTO> getAllCurrentLocation() {
        List<UsersPositionsDTO> usersPositionsDTO = new ArrayList<>();
        internalUserMap.entrySet().parallelStream().forEach(internalUser -> usersPositionsDTO.add(new UsersPositionsDTO(internalUser.getValue().getUserId(), getUserLocation(internalUser.getValue()))));
        return usersPositionsDTO;
    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/

    private final Map<String, User> internalUserMap = new HashMap<>();
    public static final String tripPricerApiKey = "test-server-api-key";

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
            user.addToVisitedLocations(new VisitedLocationDTO(user.getUserId(), new PositionDTO(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
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
