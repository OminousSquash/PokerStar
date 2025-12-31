package com.varun.pokerstars.services;

import com.varun.pokerstars.DTOs.AddPlayerDTO;
import com.varun.pokerstars.DTOs.CreatePlayerDTO;
import com.varun.pokerstars.models.Player;
import com.varun.pokerstars.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public Player createPlayer(CreatePlayerDTO createPlayerDTO) {
        Player player = new Player();
        player.setId(UUID.randomUUID().toString());
        player.setMoney(createPlayerDTO.getMoney());
        player.setName(createPlayerDTO.getUsername());
        player.setEmail("");
        return playerRepository.save(player);
    }
}
