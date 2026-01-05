package com.varun.pokerstars.gameObjects;

import com.varun.pokerstars.models.ActivePlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Data
public class GameState {
    private Deque<Card> deck = new ArrayDeque<>();
    private List<Card> community =  new ArrayList<>();
    private List<Seat> seats;
    private int dealerIdx;
    private int pot = 0;

    public GameState() {
        this.seats = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            seats.add(new Seat(SeatState.EMPTY, Optional.empty()));
        }
    }

    public void appendCommunity(int numberCards) {
        if (community == null) {
            community = new ArrayList<>();
        }
        if (deck == null || deck.size() < numberCards) {
            throw new IllegalArgumentException("Deck not initialized or insufficient cards");
        }
        for  (int i = 0; i < numberCards; i++) {
            community.add(deck.pop());
        }
    }
}
