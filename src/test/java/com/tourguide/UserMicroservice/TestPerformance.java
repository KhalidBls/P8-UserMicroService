package com.tourguide.UserMicroservice;

import com.tourguide.UserMicroservice.dto.User;
import com.tourguide.UserMicroservice.helper.InternalTestHelper;
import com.tourguide.UserMicroservice.services.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TestPerformance {

    UserService userService;


    //10 USERS : 460secondes -> 7,66 minutes      2SECONDES APRES !!!!!
    //100 USERS > à 25 min -> j'ai stopper le carnage        12 SECONDES APREs ? !!!!!!!!
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
        stopWatch.stop();
        userService.getTracker().stopTracking();

        System.out.println("*****************highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
