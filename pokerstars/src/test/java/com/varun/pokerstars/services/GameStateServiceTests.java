package com.varun.pokerstars.services;

import com.varun.pokerstars.DTOs.GameStateDTO;
import com.varun.pokerstars.DTOs.PlayerTableDTO;
import com.varun.pokerstars.gameObjects.*;
import com.varun.pokerstars.models.ActivePlayer;
import com.varun.pokerstars.models.Player;
import com.varun.pokerstars.models.PokerTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameStateServiceTest {

    @Mock
    private PlayerService playerService;

    private GameStateService gameStateService;

    private PokerTable pokerTable;

    private final String TABLE_ID = "table-1";

    @BeforeEach
    void setUp() {
        gameStateService = new GameStateService(playerService);

        Player p1 = new Player("p1", "Alice", "a@test.com", 1000);
        Player p2 = new Player("p2", "Bob", "b@test.com", 1000);

        pokerTable = new PokerTable();
        pokerTable.setId(TABLE_ID);
        pokerTable.setPlayers(List.of(p1, p2));
    }

    // ---------- CREATION ----------

    @Test
    void createGameState_initialisesDeckAndDealsTwoCardsEach() {
        GameStateDTO dto = gameStateService.createGameState(pokerTable);

        assertNotNull(dto);
        assertEquals(2, dto.getActivePlayerIds().size());

        dto.getActivePlayerIds().forEach(
                activePlayerId -> {
                    ActivePlayer activePlayer = gameStateService.getActivePlayer(new PlayerTableDTO(TABLE_ID, activePlayerId));
                    assert(activePlayer.getCards().size() == 2);
                }
        );

        // 52 cards - (2 players * 2 cards)
        assertEquals(48, dto.getDeckSize());
        assertEquals(0, dto.getCommunity().size());
    }

    // ---------- DEALING ----------

    @Test
    void dealFlop_addsThreeCommunityCards() {
        gameStateService.createGameState(pokerTable);

        GameStateDTO dto = gameStateService.dealFlop("table-1");

        assertEquals(3, dto.getCommunity().size());
        assertEquals(45, dto.getDeckSize());
    }

    @Test
    void dealTurn_addsOneCommunityCard() {
        gameStateService.createGameState(pokerTable);
        gameStateService.dealFlop("table-1");

        GameStateDTO dto = gameStateService.dealTurn("table-1");

        assertEquals(4, dto.getCommunity().size());
        assertEquals(44, dto.getDeckSize());
    }

    @Test
    void dealRiver_addsFinalCommunityCard() {
        gameStateService.createGameState(pokerTable);
        gameStateService.dealFlop("table-1");
        gameStateService.dealTurn("table-1");

        GameStateDTO dto = gameStateService.dealRiver("table-1");

        assertEquals(5, dto.getCommunity().size());
        assertEquals(43, dto.getDeckSize());
    }

    // ---------- ACTIVE PLAYER LOOKUP ----------

    @Test
    void getActivePlayer_returnsCorrectPlayer() {
        gameStateService.createGameState(pokerTable);

        PlayerTableDTO dto = new PlayerTableDTO("table-1", "p1");

        ActivePlayer ap = gameStateService.getActivePlayer(dto);

        assertEquals("p1", ap.getPlayer().getId());
    }

    @Test
    void getActivePlayer_throwsWhenPlayerNotFound() {
        gameStateService.createGameState(pokerTable);

        PlayerTableDTO dto = new PlayerTableDTO("table-1", "missing");

        assertThrows(NoSuchElementException.class,
                () -> gameStateService.getActivePlayer(dto));
    }

    // ---------- ERROR CASES ----------

    @Test
    void dealFlop_throwsWhenGameDoesNotExist() {
        assertThrows(NoSuchElementException.class,
                () -> gameStateService.dealFlop("missing-table"));
    }
}
