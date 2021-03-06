package com.tourguide.UserMicroservice.proxies;

import com.tourguide.UserMicroservice.dto.AttractionDTO;
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


@Service
public class ProxyGps {

    public List<AttractionDTO> getAttractions() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<AttractionDTO>> responseEntity =
                restTemplate.exchange("http://localhost:9091/getAttractions", HttpMethod.GET, null, new ParameterizedTypeReference<List<AttractionDTO>>() {
                });
        return responseEntity.getBody();
    }

    public VisitedLocationDTO getUserLocation(UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .getForObject("http://localhost:9091/getLocation?userId=" + userId, VisitedLocationDTO.class);
    }

}
