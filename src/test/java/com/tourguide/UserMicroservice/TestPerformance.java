package com.tourguide.UserMicroservice;

import com.tourguide.UserMicroservice.dto.AttractionDTO;
import com.tourguide.UserMicroservice.dto.PositionDTO;
import com.tourguide.UserMicroservice.dto.User;
import com.tourguide.UserMicroservice.dto.VisitedLocationDTO;
import com.tourguide.UserMicroservice.helper.InternalTestHelper;
import com.tourguide.UserMicroservice.proxies.ProxyGps;
import com.tourguide.UserMicroservice.proxies.ProxyRewards;
import com.tourguide.UserMicroservice.services.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;


@RunWith(MockitoJUnitRunner.class)
public class TestPerformance {

    @Mock
    ProxyGps proxyGps;
    @Mock
    ProxyRewards proxyRewards;
    @InjectMocks
    UserService userService;

    @BeforeEach
    public void prepare(){
        List<AttractionDTO> attractionMock = new ArrayList<>();
        attractionMock.add(new AttractionDTO("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D));
        attractionMock.add(new AttractionDTO("Jackson Hole", "Jackson Hole", "WY", 43.582767D, -110.821999D));
        attractionMock.add(new AttractionDTO("Mojave National Preserve", "Kelso", "CA", 35.141689D, -115.510399D));
        attractionMock.add(new AttractionDTO("Neyland Stadium", "Knoxville", "TN", 35.955013D, -83.925011D));
        attractionMock.add(new AttractionDTO("Kyle Field", "College Station", "TX", 30.61025D, -96.339844D));
        attractionMock.add(new AttractionDTO("San Diego Zoo", "San Diego", "CA", 32.735317D, -117.149048D));
        attractionMock.add(new AttractionDTO("Zoo Tampa at Lowry Park", "Tampa", "FL", 28.012804D, -82.469269D));
        attractionMock.add(new AttractionDTO("Franklin Park Zoo", "Boston", "MA", 42.302601D, -71.086731D));
        attractionMock.add(new AttractionDTO("El Paso Zoo", "El Paso", "TX", 31.769125D, -106.44487D));

        lenient().when(proxyRewards.getRewardPoints(any(UUID.class),any(UUID.class))).thenReturn(8);
        lenient().when(proxyGps.getAttractions()).thenReturn(attractionMock);
        lenient().when(proxyGps.getUserLocation(any(UUID.class))).thenReturn(new VisitedLocationDTO(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633"),new PositionDTO(33.81,-117.9),new Date()));
    }

    @Test
    public void highVolumeTrackLocation(){
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(10);
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
