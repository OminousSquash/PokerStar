package com.varun.pokerstars.services;

import com.varun.pokerstars.DTOs.AddPlayerDTO;
import com.varun.pokerstars.DTOs.GameStateDTO;
import com.varun.pokerstars.DTOs.PlayerTableDTO;
import com.varun.pokerstars.DTOs.CreateTableDTO;
import com.varun.pokerstars.gameObjects.*;
import com.varun.pokerstars.models.ActivePlayer;
import com.varun.pokerstars.models.Player;
import com.varun.pokerstars.models.PokerTable;
import com.varun.pokerstars.repository.TableRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PokerTableService {
    private final TableRepository tableRepository;
    private final PlayerService playerService;
    private final GameStateService gameStateService;
    private final int TABLE_SIZE = 6;

    public PokerTableService(TableRepository tableRepository,  PlayerService playerService, GameStateService gameStateService) {
        this.tableRepository = tableRepository;
        this.playerService = playerService;
        this.gameStateService = gameStateService;
    }

    public PokerTable getPokerTable(String tableId) throws  NoSuchElementException {
        PokerTable pokerTable = tableRepository.findById(tableId).orElseThrow(() -> new NoSuchElementException("Table not found"));
        return pokerTable;
    }

    public List<Player> getPlayers(String tableId) throws NoSuchElementException {
        PokerTable pokerTable = getPokerTable(tableId);
        return pokerTable.getPlayers();
    }

    public PokerTable createTable(CreateTableDTO createTableDTO) {
        PokerTable pokerTable = new PokerTable();
        pokerTable.setName(createTableDTO.getTableName());
        pokerTable.setBigBlind(createTableDTO.getBigBlind());
        pokerTable.setSmallBlind(createTableDTO.getSmallBlind());
        pokerTable.setStartingAmt(createTableDTO.getStartingAmt());
        pokerTable.setPlayers(List.of());
        pokerTable.setId(UUID.randomUUID().toString());
        pokerTable.setGameActive(false);
        gameStateService.createGameState(pokerTable);
        return tableRepository.save(pokerTable);
    }

    public GameStateDTO startGame(String tableId) throws NoSuchElementException{
        PokerTable pokerTable = getPokerTable(tableId);
        GameStateDTO gameStateDTO = gameStateService.startGame(tableId);
        pokerTable.setGameActive(true);
        return gameStateDTO;
    }

    public GameStateDTO addPlayer(AddPlayerDTO addPlayerDTO) throws NoSuchElementException, IllegalArgumentException {
        GameStateDTO gameStateDTO = gameStateService.addPlayer(addPlayerDTO);
        return gameStateDTO;
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

    public PokerTable removePlayer(PlayerTableDTO playerTableDTO) throws NoSuchElementException {
        String tableId = playerTableDTO.getTableId();
        String playerId = playerTableDTO.getPlayerId();
        PokerTable pokerTable = tableRepository.findById(tableId).orElseThrow(()->new NoSuchElementException("Table not found"));
        Player player = playerService.getPlayer(playerId);
        pokerTable.getPlayers().remove(player);
        return tableRepository.save(pokerTable);
    }

    public GameStateDTO dealFlop(String tableId) throws NoSuchElementException {
        return gameStateService.dealFlop(tableId);
    }

    public GameStateDTO dealTurn(String tableId) throws NoSuchElementException {
        return gameStateService.dealTurn(tableId);
    }

    public GameStateDTO dealRiver(String tableId) throws NoSuchElementException {
        return gameStateService.dealRiver(tableId);
    }

    public PokerHand getHand(PlayerTableDTO playerTableDTO) throws NoSuchElementException {
        return gameStateService.getHand(playerTableDTO);
    }

    public ActivePlayer getActivePlayer(PlayerTableDTO playerTableDTO) throws NoSuchElementException {
        return  gameStateService.getActivePlayer(playerTableDTO);
    }
}
