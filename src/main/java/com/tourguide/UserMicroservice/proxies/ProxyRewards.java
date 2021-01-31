package com.tourguide.UserMicroservice.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@FeignClient(name="reward-microservice",url = "localhost:8082")
public interface ProxyRewards {

    @GetMapping("/getRewardPoints")
    public int getRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId);

}
