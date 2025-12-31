package com.varun.pokerstars.repository;

import com.varun.pokerstars.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, String> {
}
