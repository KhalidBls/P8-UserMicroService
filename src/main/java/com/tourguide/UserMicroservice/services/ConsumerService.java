package com.tourguide.UserMicroservice.services;

import com.tourguide.UserMicroservice.dto.AttractionDTO;
import com.tourguide.UserMicroservice.dto.PositionDTO;
import com.tourguide.UserMicroservice.dto.User;
import com.tourguide.UserMicroservice.dto.VisitedLocationDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;


@Service
public class ConsumerService {

    public List<AttractionDTO> getAttractions() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<AttractionDTO>> responseEntity =
                restTemplate.exchange("http://localhost:8081/getAttractions", HttpMethod.GET, null, new ParameterizedTypeReference<List<AttractionDTO>>() {
                });
        return responseEntity.getBody();
    }

    public VisitedLocationDTO getUserLocation(User user) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .getForObject("http://localhost:8081/getLocation?userId=" + user.getUserId(), VisitedLocationDTO.class);
    }

    public int getRewardPoints(UUID attractionId, UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .getForObject("http://localhost:8082/getRewardPoints?attractionId=" + attractionId + "&userId="+userId , Integer.class);
    }
}
