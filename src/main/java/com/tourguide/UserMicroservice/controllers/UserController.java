package com.tourguide.UserMicroservice.controllers;

import com.tourguide.UserMicroservice.dto.ClosestsAttractionsDTO;
import com.tourguide.UserMicroservice.dto.UserRewardDTO;
import com.tourguide.UserMicroservice.dto.UsersPositionsDTO;
import com.tourguide.UserMicroservice.dto.VisitedLocationDTO;
import com.tourguide.UserMicroservice.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;

import java.util.List;


@RestController
public class UserController {

    UserService userService = new UserService();

    @GetMapping(value="/closestAttractions",produces = MediaType.APPLICATION_JSON_VALUE)
    public ClosestsAttractionsDTO getClosestAttractions(@RequestParam String userName){
        return userService.getClosestAttractionsDTO(userService.getUser(userName));
    }

    @GetMapping(value="/getLocation",produces = MediaType.APPLICATION_JSON_VALUE)
    public VisitedLocationDTO getUserLocation(@RequestParam String userName){
        return userService.getUserLocation(userService.getUser(userName));
    }

    @GetMapping(value = "/getAllCurrentLocation",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UsersPositionsDTO> getAllCurrentLocation(){
        return userService.getAllCurrentLocation();
    }

    @GetMapping(value="/getRewards",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRewardDTO> getUserRewards(@RequestParam String userName){
        return userService.getUserRewards(userService.getUser(userName));
    }

    @GetMapping(value ="/getTripDeals",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Provider> getTripDeals(@RequestParam String userName){
       return userService.getTripDeals(userService.getUser(userName));
    }

    @PutMapping(value ="/trackUserLocation",produces = MediaType.APPLICATION_JSON_VALUE)
    public void trackUserLocation(@RequestParam String userName){
        userService.trackUserLocation(userService.getUser(userName));
    }
    
}
