package com.varun.pokerstars.services;

import com.varun.pokerstars.DTOs.AddPlayerDTO;
import com.varun.pokerstars.DTOs.GameStateDTO;
import com.varun.pokerstars.DTOs.PlayerTableDTO;
import com.varun.pokerstars.gameObjects.*;
import com.varun.pokerstars.models.ActivePlayer;
import com.varun.pokerstars.models.PokerTable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameStateService {
    private final Map<String, GameState> gameStateMap = new HashMap<>();
    private final PlayerService playerService;

    public GameStateService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public GameState getGameState(String tableId) throws NoSuchElementException {
        if (gameStateMap.containsKey(tableId)) {
            return gameStateMap.get(tableId);
        }
        throw new NoSuchElementException("Table id not found");
    }

    public GameStateDTO createGameState(PokerTable pokerTable) {
        GameState gameState = new GameState();
        gameState.setDealerIdx(pokerTable.getDealerIdx());
        // init pot
        gameState.setPot(0);
        // initialize seat states
        gameStateMap.put(pokerTable.getId(), gameState);
        return new GameStateDTO(gameState);
    }


    public GameStateDTO startGame(String tableId) {
        GameState gameState = getGameState(tableId);
        // shuffle deck
        shuffleDeck(gameState);
        // set players to active
        for (Seat seat : gameState.getSeats()) {
            if (seat.getSeatState().equals(SeatState.OCCUPIED_INACTIVE)) {
                seat.setSeatState(SeatState.OCCUPIED_ACTIVE);
            }
        }
        // deal cards
        dealCards(gameState);
        return new GameStateDTO(gameState);
    }

    private void shuffleDeck(GameState gameState) {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card();
                card.setSuit(suit);
                card.setRank(rank);
                cards.add(card);
            }
        }
        Collections.shuffle(cards);
        gameState.setDeck(new ArrayDeque<>(cards));
    }

    public GameStateDTO dealFlop(String tableId){
        GameState gameState = getGameState(tableId);
        gameState.appendCommunity(3);
        for (Seat seat : gameState.getSeats()) {
            if (seat.getSeatState().equals(SeatState.OCCUPIED_ACTIVE)) {
                ActivePlayer activePlayer = seat.getMaybeActivePlayer().get();
                PokerHand hand = evaluateHand(gameState, activePlayer);
                activePlayer.setHand(hand);
            }
        }
        return new  GameStateDTO(gameState);
    }

    public GameStateDTO dealTurn(String tableId){
        GameState gameState = getGameState(tableId);
        gameState.appendCommunity(1);
        for (Seat seat : gameState.getSeats()) {
            if (seat.getSeatState().equals(SeatState.OCCUPIED_ACTIVE)) {
                ActivePlayer activePlayer = seat.getMaybeActivePlayer().get();
                PokerHand hand = evaluateHand(gameState, activePlayer);
                activePlayer.setHand(hand);
            }
        }
        return new GameStateDTO(gameState);
    }

    public GameStateDTO dealRiver(String tableId){
        GameState gameState = getGameState(tableId);
        gameState.appendCommunity(1);
        for (Seat seat : gameState.getSeats()) {
            if (seat.getSeatState().equals(SeatState.OCCUPIED_ACTIVE)) {
                ActivePlayer activePlayer = seat.getMaybeActivePlayer().get();
                PokerHand hand = evaluateHand(gameState, activePlayer);
                activePlayer.setHand(hand);
            }
        }
        return new GameStateDTO(gameState);
    }

    private void dealCards(GameState  gameState) {
        for (int i = 0; i < 2; i++) {
            for (Seat seat : gameState.getSeats()) {
                if (seat.getSeatState().equals(SeatState.OCCUPIED_ACTIVE)) {
                    ActivePlayer activePlayer = seat.getMaybeActivePlayer().get();
                    activePlayer.getCards().add(gameState.getDeck().pop());
                }
            }
        }
    }

    private void populateCountMaps(List<Card> cards, Map<Suit, Integer> suitCount, Map<Rank, Integer> rankCount){
        for (Card card : cards) {
            Rank rank = card.getRank();
            if (suitCount.containsKey(card.getSuit())) {
                suitCount.put(card.getSuit(), suitCount.get(card.getSuit()) + 1);
            } else  {
                suitCount.put(card.getSuit(), 1);
            }
            if (rankCount.containsKey(rank)) {
                rankCount.put(rank, rankCount.get(rank) + 1);
            } else   {
                rankCount.put(rank, 1);
            }
        }
    }

    public PokerHand evaluateHand(GameState gameState, ActivePlayer activePlayer) {
        List<Card> playerCards = activePlayer.getCards();
        List<Card> communityCards = gameState.getCommunity();
        Map<Suit, Integer> suitCount = new HashMap<>();
        Map<Rank, Integer> rankCount = new HashMap<>();
        populateCountMaps(playerCards, suitCount, rankCount);
        populateCountMaps(communityCards, suitCount, rankCount);
        // evaluate from best hand to worst

        // royal flush
        boolean suited = false;
        for (Map.Entry<Suit, Integer> entry : suitCount.entrySet()) {
            if (entry.getValue() == 5) {
                suited = true;
                break;
            }
        }
        if (suited &&
                rankCount.containsKey(Rank.ACE) &&
                rankCount.containsKey(Rank.KING) &&
                rankCount.containsKey(Rank.QUEEN) &&
                rankCount.containsKey(Rank.JACK) &&
                rankCount.containsKey(Rank.TEN)) {
            return PokerHand.ROYAL_FLUSH;
        }
        // straight flush
        if (suited) {
            for (Rank rank : Rank.values()) {
                List<Rank> straightCards = Rank.getStraightRanks(rank);
                boolean straightFormed = true;
                if (straightCards.isEmpty()) {
                    break;
                }
                for (Rank r : straightCards) {
                    if (!rankCount.containsKey(r)) {
                        straightFormed = false;
                        break;
                    }
                }
                if (straightFormed) {
                    return PokerHand.STRAIGHT_FLUSH;
                }
            }
        }
        // four
        for (Map.Entry<Rank, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 4) {
                return PokerHand.FOUR_OF_A_KIND;
            }
        }
        // full house
        boolean hasThree = false;
        boolean hasTwo = false;
        for (Map.Entry<Rank, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 3) {
                hasThree = true;
            } else if (entry.getValue() == 2) {
                hasTwo = true;
            }
        }
        if (hasThree && hasTwo) {
            return PokerHand.FULL_HOUSE;
        }
        // flush
        if (suited) {
            return PokerHand.FLUSH;
        }
        // straight
        for (Rank rank : Rank.values()) {
            List<Rank> straightCards = Rank.getStraightRanks(rank);
            if (straightCards.isEmpty()) {
                break;
            }
            boolean straightFormed = true;
            for (Rank r : straightCards) {
                if (!rankCount.containsKey(r)) {
                    straightFormed = false;
                    break;
                }
            }
            if (straightFormed) {
                return PokerHand.STRAIGHT_FLUSH;
            }
        }
        // three
        if (hasThree) {
            return  PokerHand.THREE_OF_A_KIND;
        }
        int pairCount = 0;
        for (Map.Entry<Rank, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 2) {
                pairCount++;
            }
        }
        // one pair & two pair
        if  (pairCount == 1) {
            return PokerHand.ONE_PAIR;
        } else if (pairCount > 1) {
            return PokerHand.TWO_PAIR;
        }
        // high card
        return PokerHand.HIGH_CARD;
    }

    public GameStateDTO addPlayer(AddPlayerDTO addPlayerDTO) throws NoSuchElementException, IllegalArgumentException {
        String tableId = addPlayerDTO.getTableId();
        String playerId = addPlayerDTO.getPlayerId();
        int seatIdx = addPlayerDTO.getSeatIdx();
        if (!(0 <= seatIdx && seatIdx <= 6)) {
            throw new IllegalArgumentException("Invalid seat index");
        }
        GameState gameState = getGameState(tableId);
        boolean alreadyPresent = gameState.getSeats().stream()
                .flatMap(seat -> seat.getMaybeActivePlayer().stream())
                .anyMatch(p -> p.getPlayer().getId().equals(playerId));
        if (alreadyPresent) {
            throw new IllegalArgumentException("Player already present in game");
        }
        if (!gameState.getSeats().get(seatIdx).getSeatState().equals(SeatState.EMPTY)) {
            throw new IllegalArgumentException("Seat already taken in game");
        }
        ActivePlayer activePlayer = new ActivePlayer(playerService.getPlayer(playerId));
        gameState.getSeats().set(seatIdx, new Seat(SeatState.OCCUPIED_INACTIVE, Optional.of(activePlayer)));
        return new GameStateDTO(gameState);
    }

    GameState getGameStateInternal(String tableId) {
        GameState gameState = getGameState(tableId);
        return gameState;
    }

    PokerHand getHand(PlayerTableDTO playerTableDTO) throws NoSuchElementException {
        GameState gameState = getGameState(playerTableDTO.getTableId());
        for (Seat seat : gameState.getSeats()) {
            if (seat.getSeatState().equals(SeatState.OCCUPIED_ACTIVE) &&
                seat.getMaybeActivePlayer().get().getPlayer().getId().equals(playerTableDTO.getPlayerId())) {
                return seat.getMaybeActivePlayer().get().getHand();
            }
        }
        throw new NoSuchElementException("No player found");
    }

    ActivePlayer getActivePlayer(PlayerTableDTO playerTableDTO) throws NoSuchElementException {
        GameState gameState = getGameState(playerTableDTO.getTableId());
        for (Seat seat : gameState.getSeats()) {
            if (!seat.getMaybeActivePlayer().isEmpty() && seat.getMaybeActivePlayer().get().getPlayer().getId().equals(playerTableDTO.getPlayerId())) {
                return seat.getMaybeActivePlayer().get();
            }
        }
        throw new NoSuchElementException("No player found");
    }
}
