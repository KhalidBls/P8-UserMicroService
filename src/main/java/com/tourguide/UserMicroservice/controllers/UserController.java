package com.tourguide.UserMicroservice.controllers;

import com.tourguide.UserMicroservice.dto.ClosestsAttractionsDTO;
import com.tourguide.UserMicroservice.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    UserService userService = new UserService();

    @GetMapping("/closestAttractions")
    public ClosestsAttractionsDTO getClosestAttractions(@RequestParam String userName){
        return userService.getClosestAttractionsDTO(userName);
    }

}
