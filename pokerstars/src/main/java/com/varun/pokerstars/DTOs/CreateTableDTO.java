package com.varun.pokerstars.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTableDTO {
    private String tableName;
    private int bigBlind;
    private int smallBlind;
    private int startingAmt;
}
