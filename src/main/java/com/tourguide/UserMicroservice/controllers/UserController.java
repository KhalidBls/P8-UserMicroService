package com.tourguide.UserMicroservice.controllers;

import com.tourguide.UserMicroservice.dto.Attraction;
import com.tourguide.UserMicroservice.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    UserService userService = new UserService();

    @GetMapping("/closestAttractions")
    public List<Attraction> getClosestAttractions(@RequestParam String userName){
        return userService.getClosestAttractions(userName);
    }

}
