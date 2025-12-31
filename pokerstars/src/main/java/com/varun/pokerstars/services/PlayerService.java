package com.varun.pokerstars.services;

import com.varun.pokerstars.models.Player;
import com.varun.pokerstars.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private PlayerRepository playerRepository;
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public Player findById(String id) {
        return playerRepository.findById(id).orElse(null);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void deleteById(String id) {
        if  (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
        }
    }
}
