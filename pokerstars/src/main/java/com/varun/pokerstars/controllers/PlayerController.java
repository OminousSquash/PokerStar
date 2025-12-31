package com.varun.pokerstars.controllers;

import com.varun.pokerstars.services.PlayerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = "*")
public class PlayerController {
    private PlayerService playerService;
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
}
