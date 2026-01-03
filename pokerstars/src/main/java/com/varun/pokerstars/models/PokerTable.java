package com.varun.pokerstars.models;

import com.varun.pokerstars.gameObjects.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PokerTable {

    @Id
    private String id;

    private String name;

    @OneToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "poker_table_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players = new ArrayList<>();

    @Transient
    private List<ActivePlayer> activePlayers = new ArrayList<>();

    private int smallBlind;
    private int bigBlind;
    private int startingAmt;
    @Column(nullable = false)
    private boolean gameActive = false;
    public void appendCommunity(int numberCards) {
    }
}
