package com.varun.pokerstars.DTOs;

import com.varun.pokerstars.gameObjects.Card;
import com.varun.pokerstars.gameObjects.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Deque;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateDTO {
    private Deque<Card> deck;
    private List<Card> community;
    private List<String> activePlayerIds;
    private int pot;

    public GameStateDTO(GameState gameState) {
        this.deck = gameState.getDeck();
        this.community = gameState.getCommunity();
        this.activePlayerIds = gameState.getActivePlayers()
                .stream()
                .map(activePlayer -> activePlayer.getPlayer().getId())
                .toList();
        this.pot = gameState.getPot();
    }

    public int getDeckSize() {
        return deck.size();
    }
}
