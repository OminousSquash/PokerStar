package com.varun.pokerstars.gameObjects;


import com.varun.pokerstars.models.ActivePlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    private SeatState seatState;
    private Optional<ActivePlayer> maybeActivePlayer;
}
