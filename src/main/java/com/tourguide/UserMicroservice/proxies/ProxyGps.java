package com.tourguide.UserMicroservice.proxies;

import com.tourguide.UserMicroservice.dto.AttractionDTO;
import com.tourguide.UserMicroservice.dto.PositionDTO;
import com.tourguide.UserMicroservice.dto.User;
import com.tourguide.UserMicroservice.dto.VisitedLocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;


@FeignClient(name="gps-microservice",url = "localhost:8081")
public interface ProxyGps {

    @GetMapping("/getLocation")
    VisitedLocationDTO getLocation(@RequestParam UUID userId);

    @GetMapping("/getAttractions")
    List<AttractionDTO> getAttractions();

}
