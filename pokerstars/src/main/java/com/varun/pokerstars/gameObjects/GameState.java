package com.varun.pokerstars.gameObjects;

import com.varun.pokerstars.models.ActivePlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
    private Deque<Card> deck;
    private List<Card> community;
    private List<ActivePlayer> activePlayers;
    private int pot;

    public void appendCommunity(int numberCards) {
        if (community == null) {
            community = new ArrayList<>();
        }
        if (deck == null || deck.size() < numberCards) {
            throw new IllegalArgumentException("Deck not initialized or insufficient cards");
        }
        for  (int i = 0; i < numberCards; i++) {
            community.add(deck.removeFirst());
        }
    }
}
