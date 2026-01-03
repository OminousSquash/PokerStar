package com.varun.pokerstars.services;

import com.varun.pokerstars.DTOs.GameStateDTO;
import com.varun.pokerstars.DTOs.PlayerTableDTO;
import com.varun.pokerstars.gameObjects.*;
import com.varun.pokerstars.models.ActivePlayer;
import com.varun.pokerstars.models.Player;
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

    private GameState getGameState(String tableId) throws NoSuchElementException {
        if (gameStateMap.containsKey(tableId)) {
            return gameStateMap.get(tableId);
        }
        throw new NoSuchElementException("Table id not found");
    }

    public GameStateDTO createGameState(PokerTable pokerTable) {
        GameState gameState = new GameState();
        // create deck
        shuffleDeck(gameState);
        // create community cards
        gameState.setCommunity(List.of());
        // init pot
        gameState.setPot(0);
        // assign players
        gameState.setActivePlayers(pokerTable.getPlayers()
                .stream()
                .map(player -> new ActivePlayer(player))
                .toList());
        // deal cards
        GameStateDTO gameStateDTO = dealCards(gameState);
        gameStateMap.put(pokerTable.getId(), gameState);
        return gameStateDTO;
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
        for (ActivePlayer activePlayer : gameState.getActivePlayers()) {
            evaluateHand(gameState, activePlayer);
        }
        return new  GameStateDTO(gameState);
    }

    public GameStateDTO dealTurn(String tableId){
        GameState gameState = getGameState(tableId);
        gameState.appendCommunity(1);
        for (ActivePlayer activePlayer : gameState.getActivePlayers()) {
            evaluateHand(gameState, activePlayer);
        }
        return new GameStateDTO(gameState);
    }

    public GameStateDTO dealRiver(String tableId){
        GameState gameState = getGameState(tableId);
        gameState.appendCommunity(1);
        for (ActivePlayer activePlayer : gameState.getActivePlayers()) {
            evaluateHand(gameState, activePlayer);
        }
        return new GameStateDTO(gameState);
    }

    public ActivePlayer getActivePlayer(PlayerTableDTO playerTableDTO){
        GameState gameState = getGameState(playerTableDTO.getTableId());
        Optional<ActivePlayer> activePlayer = gameState
                .getActivePlayers()
                .stream()
                .filter(x -> x.getPlayer()
                        .getId()
                        .equals(playerTableDTO.getPlayerId()))
                .findFirst();
        if  (activePlayer.isPresent()) {
            return activePlayer.get();
        }
        throw new NoSuchElementException("Player not found");
    }

    private GameStateDTO dealCards(GameState  gameState) {
        List<ActivePlayer> activePlayers = gameState.getActivePlayers();
        for (int i = 0; i < 2; i++) {
            for (ActivePlayer activePlayer : activePlayers) {
                activePlayer.getCards().add(gameState.getDeck().pop());
            }
        }
        return new GameStateDTO(gameState);
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
}
