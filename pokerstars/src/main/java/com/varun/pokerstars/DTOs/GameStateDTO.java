package com.varun.pokerstars.DTOs;

import com.varun.pokerstars.gameObjects.Card;
import com.varun.pokerstars.gameObjects.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateDTO {
    private Deque<Card> deck;
    private List<Card> community;
    private List<SeatDTO> seats;
    private int pot;

    public GameStateDTO(GameState gameState) {
        this.deck = gameState.getDeck();
        this.community = gameState.getCommunity();
        this.seats = gameState.getSeats().stream().map(seat -> new SeatDTO(seat)).toList();
        this.pot = gameState.getPot();
    }

    public int getDeckSize() {
        return deck.size();
    }
}
