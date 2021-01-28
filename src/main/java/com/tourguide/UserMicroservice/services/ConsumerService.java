package com.tourguide.UserMicroservice.services;

import com.tourguide.UserMicroservice.dto.AttractionDTO;
import com.tourguide.UserMicroservice.dto.PositionDTO;
import com.tourguide.UserMicroservice.dto.User;
import gpsUtil.location.VisitedLocation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;



@Service
public class ConsumerService {

    public List<AttractionDTO> getAttractions() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<AttractionDTO>> responseEntity =
                restTemplate.exchange("http://localhost:8081/getAttractions", HttpMethod.GET, null, new ParameterizedTypeReference<List<AttractionDTO>>() {
                });
        return responseEntity.getBody();
    }

    public PositionDTO getUserLocation(User user) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .getForObject("http://localhost:8081/getLocation?userId=" + user.getUserId(), PositionDTO.class);
    }
}
