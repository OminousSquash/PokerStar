package com.varun.pokerstars.DTOs;

import com.varun.pokerstars.gameObjects.GameState;
import com.varun.pokerstars.gameObjects.Seat;
import com.varun.pokerstars.gameObjects.SeatState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.html.Option;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
    private Seat seat;
}
