package com.varun.pokerstars.services;

import com.varun.pokerstars.DTOs.AddPlayerDTO;
import com.varun.pokerstars.DTOs.CreateTableDTO;
import com.varun.pokerstars.models.Player;
import com.varun.pokerstars.models.PokerTable;
import com.varun.pokerstars.repository.PlayerRepository;
import com.varun.pokerstars.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PokerTableService {
    private final TableRepository tableRepository;
    private final PlayerRepository playerRepository;

    public PokerTableService(TableRepository tableRepository,  PlayerRepository playerRepository) {
        this.tableRepository = tableRepository;
        this.playerRepository = playerRepository;
    }

    public PokerTable createTable(CreateTableDTO createTableDTO) {
        PokerTable pokerTable = new PokerTable();
        pokerTable.setName(createTableDTO.getTableName());
        pokerTable.setBigBlind(createTableDTO.getBigBlind());
        pokerTable.setSmallBlind(createTableDTO.getSmallBlind());
        pokerTable.setStartingAmt(createTableDTO.getStartingAmt());
        pokerTable.setPlayers(List.of());
        pokerTable.setId(UUID.randomUUID().toString());
        return tableRepository.save(pokerTable);
    }

    public PokerTable addPlayer(AddPlayerDTO addPlayerDTO) throws NoSuchElementException, IllegalStateException
    {
        String tableId = addPlayerDTO.getTableId();
        String playerId = addPlayerDTO.getPlayerId();
        PokerTable pokerTable = tableRepository.findById(tableId).orElse(null);
        if  (pokerTable == null) {
            throw new NoSuchElementException("Table not found");
        }
        if (!playerRepository.findById(playerId).isPresent()) {
            throw new NoSuchElementException("Player not found");
        }
        if (pokerTable.getPlayers().contains(playerId)) {
            throw new IllegalStateException("Player already exists");
        }
        Player player = playerRepository.findById(playerId).orElse(null);
        pokerTable.getPlayers().add(player);
        return tableRepository.save(pokerTable);
    }

    public List<PokerTable> getAllTables() {
        return tableRepository.findAll();
    }

    public int getBuyIn(String tableId) throws NoSuchElementException {
        PokerTable pokerTable = tableRepository.findById(tableId).orElse(null);
        if (pokerTable == null) {
            throw new NoSuchElementException("Table not found");
        }
        return pokerTable.getStartingAmt();
    }
}
