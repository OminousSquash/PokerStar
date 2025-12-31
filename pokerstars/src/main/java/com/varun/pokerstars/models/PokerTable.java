package com.varun.pokerstars.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<Player> players;

    private int smallBlind;
    private int bigBlind;
    private int startingAmt;
}
