package com.tourguide.UserMicroservice;

import com.tourguide.UserMicroservice.dto.User;
import com.tourguide.UserMicroservice.helper.InternalTestHelper;
import com.tourguide.UserMicroservice.services.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TestPerformance {

    UserService userService;

    @Test
    public void highVolumeTrackLocation(){
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(10);
        userService = new UserService();
        List<User> allUsers = userService.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        IntStream.range(0, allUsers.size())
                .forEach(index -> {
                        userService.trackUserLocation(allUsers.get(index));
                });
        stopWatch.stop();
        userService.getTracker().stopTracking();

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
