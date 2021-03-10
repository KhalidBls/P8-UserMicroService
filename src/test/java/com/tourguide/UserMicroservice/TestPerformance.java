package com.tourguide.UserMicroservice;

import com.tourguide.UserMicroservice.dto.*;
import com.tourguide.UserMicroservice.helper.InternalTestHelper;
import com.tourguide.UserMicroservice.services.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestPerformance {

    UserService userService;

    // NOMBRE UTILISATEURS / AVANT OPTIMISATION / APRES OPTIMISATION
    //  10 USERS :              7s                  0s
    //  100 USERS :             14s                 0s
    //  1000 USERS :            1min28              0s
    //  10000 USERS :           12min58             137s
    //100 000 USERS :           PLUS DE 50min       23min40s
    @Test
    public void highVolumeTrackLocation(){
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(100);
        userService = new UserService();

        List<User> allUsers = new ArrayList<>();
        allUsers = userService.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for(User user : allUsers) {
            userService.trackUserLocation(user);
        }

        allUsers.forEach(u -> {
            while (!(u.getVisitedLocations().size() > 3)){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        stopWatch.stop();
        userService.getTracker().stopTracking();

        System.out.println("*****************highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    // NOMBRE UTILISATEURS / AVANT OPTIMISATION / APRES OPTIMISATION
    //  10 USERS :              6s                  1s
    //  100 USERS :             53s                 1s
    //  1000 USERS :            3min08              1s
    //  10_000 USERS :           17min14            2s
    // 100 000 USERS :           PLUS DE 50min       13s
    @Test
    public void highVolumeGetRewards() {
        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(100);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        userService = new UserService();

        AttractionDTO attraction = userService.getAttractionsFromProxy().get(0);
        List<User> allUsers = new ArrayList<>();
        allUsers = userService.getAllUsers();
        allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocationDTO(u.getUserId(), new PositionDTO(attraction.getLatitude(),attraction.getLongitude()), new Date())));

        allUsers.forEach(u -> {
            userService.calculateRewards(u);
        });

        allUsers.forEach(u -> {
            while (u.getUserRewards().isEmpty()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        stopWatch.stop();
        userService.getTracker().stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
