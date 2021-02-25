package com.tourguide.UserMicroservice.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class ProxyRewards {

    public Integer getRewardPoints(UUID attractionId, UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .getForObject("http://localhost:9092/getRewardPoints?attractionId=" + attractionId + "&userId="+userId , Integer.class);
    }

}
