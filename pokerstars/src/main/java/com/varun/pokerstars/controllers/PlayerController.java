package com.varun.pokerstars.controllers;

import com.varun.pokerstars.DTOs.CreatePlayerDTO;
import com.varun.pokerstars.models.Player;
import com.varun.pokerstars.services.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = "*")
public class PlayerController {
    private PlayerService playerService;
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPlayer(@RequestBody CreatePlayerDTO createPlayerDTO) {
        try {
            Player p = playerService.createPlayer(createPlayerDTO);
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
