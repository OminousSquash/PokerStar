package com.varun.pokerstars.models;

import com.varun.pokerstars.gameObjects.Card;
import com.varun.pokerstars.gameObjects.PokerHand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivePlayer {
    private Player player;

    private List<Card> cards;

    private PokerHand hand;

    public ActivePlayer(Player player) {
        this.player = player;
        this.cards = new ArrayList<>();
        this.hand = null;
    }
}
