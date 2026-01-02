package com.varun.pokerstars.controllers;

import com.varun.pokerstars.DTOs.AddPlayerDTO;
import com.varun.pokerstars.DTOs.CreateTableDTO;
import com.varun.pokerstars.DTOs.RemovePlayerDTO;
import com.varun.pokerstars.models.PokerTable;
import com.varun.pokerstars.services.PokerTableService;
import org.hibernate.Remove;
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
    public ResponseEntity<?>  addPlayer(@RequestBody AddPlayerDTO addPlayerDTO) {
        try {
            PokerTable pokerTable =  pokerTableService.addPlayer(addPlayerDTO);
            return ResponseEntity.ok(pokerTable);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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

    @GetMapping("/{id}/buyIn")
    public ResponseEntity<?> getPokerTable(@PathVariable String id) {
        try {
            int tableBuyIn = pokerTableService.getBuyIn(id);
            return ResponseEntity.ok(tableBuyIn);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/removePlayer")
    public ResponseEntity<?> removePlayer(@RequestBody RemovePlayerDTO removePlayerDTO) {
        try {
            PokerTable pokerTable = pokerTableService.removePlayer(removePlayerDTO);
            return  ResponseEntity.ok(pokerTable);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
