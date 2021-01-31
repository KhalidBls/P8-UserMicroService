package com.tourguide.UserMicroservice.controllers;

import com.tourguide.UserMicroservice.dto.ClosestsAttractionsDTO;
import com.tourguide.UserMicroservice.dto.UserRewardDTO;
import com.tourguide.UserMicroservice.dto.UsersPositionsDTO;
import com.tourguide.UserMicroservice.dto.VisitedLocationDTO;
import com.tourguide.UserMicroservice.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;

import java.util.List;


@RestController
public class UserController {

    UserService userService = new UserService();

    @GetMapping("/closestAttractions")
    public ClosestsAttractionsDTO getClosestAttractions(@RequestParam String userName){
        return userService.getClosestAttractionsDTO(userService.getUser(userName));
    }

    @GetMapping("/getLocation")
    public VisitedLocationDTO getUserLocation(@RequestParam String userName){
        return userService.getUserLocation(userService.getUser(userName));
    }

    @GetMapping("/getAllCurrentLocation")
    public List<UsersPositionsDTO> getAllCurrentLocation(){
        return userService.getAllCurrentLocation();
    }

    @GetMapping("/getRewards")
    public List<UserRewardDTO> getUserRewards(@RequestParam String userName){
        return userService.getUserRewards(userService.getUser(userName));
    }

    @GetMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName){
       return userService.getTripDeals(userService.getUser(userName));
    }
    
}
