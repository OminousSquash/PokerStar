package com.varun.pokerstars.gameObjects;

import lombok.Getter;

@Getter
public enum PokerHand {
    HIGH_CARD(1, "High Card"),
    ONE_PAIR(2, "One Pair"),
    TWO_PAIR(3, "Two Pair"),
    THREE_OF_A_KIND(4, "Three of a Kind"),
    STRAIGHT(5, "Straight"),
    FLUSH(6, "Flush"),
    FULL_HOUSE(7, "Full House"),
    FOUR_OF_A_KIND(8, "Four of a Kind"),
    STRAIGHT_FLUSH(9, "Straight Flush"),
    ROYAL_FLUSH(10, "Royal Flush");

    private final String name;
    private final int rank;

    PokerHand(int rank, String name) {
        this.name = name;
        this.rank = rank;
    }
}
