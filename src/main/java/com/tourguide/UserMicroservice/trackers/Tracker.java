package com.tourguide.UserMicroservice.trackers;

import com.tourguide.UserMicroservice.dto.User;
import com.tourguide.UserMicroservice.services.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Tracker extends Thread{
    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(3000);
    private final UserService userService;
    private boolean stop = false;

    public Tracker(UserService userService) {
        this.userService = userService;
        executorService.submit(this);
    }

    /**
     * Assures to shut down the Tracker thread
     */
    public void stopTracking() {
        stop = true;
        executorService.shutdownNow();
    }

    @Override
    public void run() {

        while (!stop){
            userService.getAllUsers().forEach(user ->{
                userService.trackUserLocation(user);
            });

        }
    }
}
