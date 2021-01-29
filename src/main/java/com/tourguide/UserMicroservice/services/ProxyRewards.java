package com.tourguide.UserMicroservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class ProxyRewards {

    public int getRewardPoints(UUID attractionId, UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .getForObject("http://localhost:8082/getRewardPoints?attractionId=" + attractionId + "&userId="+userId , Integer.class);
    }

}
