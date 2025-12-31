package com.varun.pokerstars.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private List<Player> players;

    private int smallBlind;
    private int bigBlind;
    private int startingAmt;
}
