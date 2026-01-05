package com.varun.pokerstars.services;

import com.varun.pokerstars.DTOs.AddPlayerDTO;
import com.varun.pokerstars.DTOs.GameStateDTO;
import com.varun.pokerstars.DTOs.PlayerTableDTO;
import com.varun.pokerstars.gameObjects.SeatState;
import com.varun.pokerstars.models.Player;
import com.varun.pokerstars.models.PokerTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameStateServiceTest {

    private static final String TABLE_ID = "table-1";
    private static final String PLAYER_1 = "player-1";
    private static final String PLAYER_2 = "player-2";

    @Mock
    private PlayerService playerService;

    @Mock
    private PokerTableService pokerTableService;

    @InjectMocks
    private GameStateService gameStateService;

    private PokerTable pokerTable;

    @BeforeEach
    void setUp() {
        pokerTable = new PokerTable();
        pokerTable.setId(TABLE_ID);
        pokerTable.setSmallBlind(10);
        pokerTable.setBigBlind(20);
        gameStateService.createGameState(pokerTable);
    }

    // ---------- GAME CREATION ----------

    @Test
    void startGame_createsGameStateAndDealsTwoCardsEach() {
        Player p1 = new Player(PLAYER_1, "A", "A@gmail.com", 1000);
        Player p2 = new Player(PLAYER_2, "B", "B@gmail.com", 1000);

        when(playerService.getPlayer(PLAYER_1)).thenReturn(p1);
        when(playerService.getPlayer(PLAYER_2)).thenReturn(p2);

        gameStateService.addPlayer(new AddPlayerDTO(PLAYER_1, TABLE_ID, 0));
        gameStateService.addPlayer(new AddPlayerDTO(PLAYER_2, TABLE_ID, 1));

        GameStateDTO dto = gameStateService.startGame(TABLE_ID);

        assertNotNull(dto);
        assertEquals(48, dto.getDeckSize()); // 52 - 4 hole cards
        assertEquals(2, dto.getSeats().stream().filter(seat -> !seat.getSeat().getSeatState().equals(SeatState.EMPTY)).count());
    }

    // ---------- GAME FLOW ----------

    @Test
    void dealFlop_addsThreeCommunityCards() {
        startBasicGame();

        GameStateDTO dto = gameStateService.dealFlop(TABLE_ID);

        assertEquals(3, dto.getCommunity().size());
        assertEquals(45, dto.getDeckSize());
    }

    @Test
    void dealTurn_addsFourthCommunityCard() {
        startBasicGame();
        gameStateService.dealFlop(TABLE_ID);

        GameStateDTO dto = gameStateService.dealTurn(TABLE_ID);

        assertEquals(4, dto.getCommunity().size());
    }

    @Test
    void dealRiver_addsFifthCommunityCard() {
        startBasicGame();
        gameStateService.dealFlop(TABLE_ID);
        gameStateService.dealTurn(TABLE_ID);

        GameStateDTO dto = gameStateService.dealRiver(TABLE_ID);

        assertEquals(5, dto.getCommunity().size());
    }

    // ---------- PLAYER LOOKUPS ----------

    @Test
    void getActivePlayer_returnsCorrectPlayer() {
        startBasicGame();

        PlayerTableDTO dto = new PlayerTableDTO(TABLE_ID, PLAYER_1);

        assertEquals(
                PLAYER_1,
                gameStateService.getActivePlayer(dto).getPlayer().getId()
        );
    }

    @Test
    void getActivePlayer_throwsIfPlayerMissing() {
        startBasicGame();

        PlayerTableDTO dto = new PlayerTableDTO(TABLE_ID, "missing");

        assertThrows(NoSuchElementException.class,
                () -> gameStateService.getActivePlayer(dto));
    }

    // ---------- ERROR CASES ----------

    @Test
    void dealFlop_throwsIfGameNotStarted() {
        assertThrows(NoSuchElementException.class,
                () -> gameStateService.dealFlop("missing-table"));
    }

    // ---------- HELPERS ----------

    private void startBasicGame() {
        Player p1 = new Player(PLAYER_1, "A", "A@gmail.com", 1000);
        Player p2 = new Player(PLAYER_2, "B", "B@gmail.com", 1000);

        when(playerService.getPlayer(PLAYER_1)).thenReturn(p1);
        when(playerService.getPlayer(PLAYER_2)).thenReturn(p2);

        gameStateService.addPlayer(new AddPlayerDTO(PLAYER_1, TABLE_ID, 0));
        gameStateService.addPlayer(new AddPlayerDTO(PLAYER_2, TABLE_ID, 1));

        gameStateService.startGame(TABLE_ID);
    }
}
