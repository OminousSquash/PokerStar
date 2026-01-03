package com.varun.pokerstars.controllers;

import com.varun.pokerstars.DTOs.GameStateDTO;
import com.varun.pokerstars.DTOs.PlayerTableDTO;
import com.varun.pokerstars.DTOs.CreateTableDTO;
import com.varun.pokerstars.DTOs.TableIdDTO;
import com.varun.pokerstars.gameObjects.Card;
import com.varun.pokerstars.models.ActivePlayer;
import com.varun.pokerstars.DTOs.GameStateDTO;
import com.varun.pokerstars.models.PokerTable;
import com.varun.pokerstars.services.PokerTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/table")
@CrossOrigin(origins = "*")
public class PokerTableController {
    private PokerTableService pokerTableService;
    public PokerTableController(PokerTableService pokerTableService) {
        this.pokerTableService = pokerTableService;
    }

    @PostMapping("/create")
    public ResponseEntity<PokerTable> createPokerTable(@RequestBody CreateTableDTO createTableDTO) {
        PokerTable pokerTable =  pokerTableService.createTable(createTableDTO);
        return ResponseEntity.ok(pokerTable);
    }

    @PostMapping("/addPlayer")
    public ResponseEntity<?>  addPlayer(@RequestBody PlayerTableDTO playerTableDTO) {
        try {
            PokerTable pokerTable =  pokerTableService.addPlayer(playerTableDTO);
            return ResponseEntity.ok(pokerTable);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getPokerTable(@RequestBody TableIdDTO tableId) {
        try {
            PokerTable pokerTable = pokerTableService.getPokerTable(tableId.getTableId());
            return ResponseEntity.ok(pokerTable);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPokerTable() {
        try {
            List<PokerTable> pokerTables = pokerTableService.getAllTables();
            return ResponseEntity.ok(pokerTables);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/buyIn")
    public ResponseEntity<?> getBuyIn(@RequestBody TableIdDTO tableIdDTO) {
        try {
            int tableBuyIn = pokerTableService.getBuyIn(tableIdDTO.getTableId());
            return ResponseEntity.ok(tableBuyIn);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/removePlayer")
    public ResponseEntity<?> removePlayer(@RequestBody PlayerTableDTO playerTableDTO) {
        try {
            PokerTable pokerTable = pokerTableService.removePlayer(playerTableDTO);
            return  ResponseEntity.ok(pokerTable);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/startGame")
    public ResponseEntity<?> startGame(@RequestBody TableIdDTO tableId) {
        try {
            GameStateDTO gameStateDTO = pokerTableService.startGame(tableId.getTableId());
            return  ResponseEntity.ok(gameStateDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/dealFlop")
    public ResponseEntity<?> dealFlop(@RequestBody TableIdDTO tableId) {
        try {
            GameStateDTO gameStateDTO = pokerTableService.dealFlop(tableId.getTableId());
            return  ResponseEntity.ok(gameStateDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/activePlayer")
    public ResponseEntity<?> getHand(@RequestBody  PlayerTableDTO playerTableDTO) {
        try {
            ActivePlayer activePlayerDetails = pokerTableService.getActivePlayerDetails(playerTableDTO);
            return  ResponseEntity.ok(activePlayerDetails);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/dealTurn")
    public ResponseEntity<?> dealTurn(@RequestBody TableIdDTO tableId) {
        try {
            GameStateDTO gameStateDTO= pokerTableService.dealTurn(tableId.getTableId());
            return ResponseEntity.ok(gameStateDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/dealRiver")
    public ResponseEntity<?> dealRiver(@RequestBody TableIdDTO tableId) {
        try {
            GameStateDTO gameStateDTO= pokerTableService.dealRiver(tableId.getTableId());
            return ResponseEntity.ok(gameStateDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/bet")
    public ResponseEntity<?> bet(@RequestBody PlayerTableDTO playerTableDTO) {
        return null;
    }

    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody PlayerTableDTO playerTableDTO) {
        return null;
    }

    @PostMapping("/fold")
    public ResponseEntity<?> fold(@RequestBody PlayerTableDTO playerTableDTO) {
        return null;
    }

    @GetMapping("/playerHand")
    public ResponseEntity<?> getPlayerHand(@RequestBody PlayerTableDTO playerTableDTO) {
        return null;
    }
}
