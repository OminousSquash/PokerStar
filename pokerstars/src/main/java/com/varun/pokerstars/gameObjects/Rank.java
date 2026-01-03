package com.varun.pokerstars.gameObjects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Rank {

    TWO("Two", 2),
    THREE("Three", 3),
    FOUR("Four", 4),
    FIVE("Five", 5),
    SIX("Six", 6),
    SEVEN("Seven", 7),
    EIGHT("Eight", 8),
    NINE("Nine", 9),
    TEN("Ten", 10),
    JACK("Jack", 11),
    QUEEN("Queen", 12),
    KING("King", 13),
    ACE("Ace", 14);

    private final String name;
    private final int rank;

    Rank(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public static List<Rank> getStraightRanks(Rank start) {
        List<Rank> ranks = new ArrayList<>();
        if (start.rank >= 11) {
            return ranks;
        }
        int targetRank = start.rank;
        for (Rank rank : Rank.values()) {
            if (targetRank == rank.rank) {
                ranks.add(rank);
                targetRank++;
                if (ranks.size() == 5) {
                    break;
                }
            }
        }
        return  ranks;
    }
}
