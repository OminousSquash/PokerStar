package com.varun.pokerstars.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPlayerDTO {
    private String tableId;
    private String playerId;
}
