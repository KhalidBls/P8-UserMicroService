package com.tourguide.UserMicroservice.services;

import com.tourguide.UserMicroservice.dto.*;
import com.tourguide.UserMicroservice.proxies.ProxyGps;
import com.tourguide.UserMicroservice.proxies.ProxyRewards;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
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

        attractionMock.forEach(attractionDTO -> attractionDTO.setAttractionId(UUID.randomUUID()));

        lenient().when(proxyRewards.getRewardPoints(any(UUID.class),any(UUID.class))).thenReturn(8);
        lenient().when(proxyGps.getAttractions()).thenReturn(attractionMock);
        lenient().when(proxyGps.getUserLocation(any(UUID.class))).thenReturn(new VisitedLocationDTO(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633"),new PositionDTO(33.81,-117.9),new Date()));
    }

    @Test
    public void getClosestAttractionTestShouldReturnThe5AttractionsClosest(){
        //Given
        User user = new User(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633"),"internalUser","0123456789","internal@user.com");
        //When
        ClosestsAttractionsDTO closestsAttractionsDTO = userService.getClosestAttractionsDTO(user);
        //Then
        verify(proxyGps, times(1)).getAttractions();
        verify(proxyRewards, times(5)).getRewardPoints(any(UUID.class),eq(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633")));
        Assertions.assertEquals(closestsAttractionsDTO.getAttractionDTOList().size(),5);
        Assertions.assertEquals(closestsAttractionsDTO.getAttractionDTOList().get(0).getAttractionName(),"Disneyland");
    }

    @Test
    public void getUserLocationShouldReturnUserPosition(){
        //Given
        User user = new User(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633"),"internalUser","0123456789","internal@user.com");
        //When
        VisitedLocationDTO visitedLocationDTO = userService.getUserLocation(user);
        //Then
        verify(proxyGps, times(1)).getUserLocation(eq(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633")));
        Assertions.assertEquals(visitedLocationDTO.location.getLatitude(),33.81);
        Assertions.assertEquals(visitedLocationDTO.location.getLongitude(),-117.9);
    }

    @Test
    public void calculateRewardShouldAddRewardToUserWhenNeverVisited(){
        //Given
        User user = new User(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633"),"internalUser","0123456789","internal@user.com");
        user.addToVisitedLocations(new VisitedLocationDTO(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633"),new PositionDTO(33.817595D,-117.922008D),new Date()));
        //When
        Assertions.assertTrue(user.getUserRewards().size()==0);
        userService.calculateRewards(user);
        //Then
        verify(proxyRewards, times(1)).getRewardPoints(any(UUID.class),eq(UUID.fromString("211468fa-b61f-4f9d-999f-fb17a2896633")));  //9 correspond aux nombres d'attraction mocker
        Assertions.assertTrue(user.getUserRewards().size()>0);
    }

}
