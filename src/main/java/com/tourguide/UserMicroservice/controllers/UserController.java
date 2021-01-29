package com.tourguide.UserMicroservice.controllers;

import com.tourguide.UserMicroservice.dto.ClosestsAttractionsDTO;
import com.tourguide.UserMicroservice.dto.UsersPositionsDTO;
import com.tourguide.UserMicroservice.dto.VisitedLocationDTO;
import com.tourguide.UserMicroservice.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {

    UserService userService = new UserService();

    @GetMapping("/closestAttractions")
    public ClosestsAttractionsDTO getClosestAttractions(@RequestParam String userName){
        return userService.getClosestAttractionsDTO(userName);
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
    public void getUserRewards(@RequestParam String userName){
        /*
        -	 Prend en paramètre un nom d’utilisateur
        -	Retourne la liste des attractions visiter par l’utilisateur avec la position de l’attraction et les points que l’utilisateur a gagné en la visitant
         */
    }

    @GetMapping("/getTripDeals")
    public void getTripDeals(@RequestParam String userName){
        /*
        -	Prend en paramètre un nom d’utilisateur
        -	Retourne l’objet User concerné avec son id, son username, numéro de téléphone, son adresse, ses points etc…
         */
    }
    
}
