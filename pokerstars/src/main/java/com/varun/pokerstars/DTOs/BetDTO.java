package com.varun.pokerstars.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetDTO {
    private String playerId;
    private String tableId;
    private int betAmt;
}
