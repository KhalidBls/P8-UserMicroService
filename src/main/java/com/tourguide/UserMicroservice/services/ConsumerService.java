package com.tourguide.UserMicroservice.services;

import com.tourguide.UserMicroservice.dto.Attraction;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;



@Service
public class ConsumerService {

    public List<Attraction> getAttractions() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Attraction>> responseEntity =
                restTemplate.exchange("http://localhost:8081/getAttractions", HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                });
        return responseEntity.getBody();
    }

}
