package service.impl;

import DTO.GameDTO;
import entity.Card;
import entity.Game;
import entity.GameInfo;
import entity.Player;
import enums.GameEvent;
import enums.GameStatus;
import exception.GameNotFoundException;
import exception.GameUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.GameRepository;
import service.CardService;
import service.GameEventListener;
import service.GameService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private CardService cardService;

    @Autowired
    private GameRepository repository;

    @Autowired
    private GameEventListener listener;

    private List<GameStatus> allowedGameStatusToUpdate = Arrays.asList(GameStatus.CREATED, GameStatus.WAITING_FOR_PLAYERS);

    @Override
    public Game createGame(GameDTO gameDTO) {
        try {
            UUID id = UUID.randomUUID();
            List<Player> playerList = gameDTO.getPlayers().stream()
                    .map(playerDTO -> Player.builder().id(playerDTO.getId()).build())
                    .collect(Collectors.toList());
            int deck = gameDTO.getDeck() == null ? 1 : gameDTO.getDeck();
            int cardPerPlayer = gameDTO.getNumberOfCardsPerPlayer() == null ? 2 : gameDTO.getNumberOfCardsPerPlayer();
            Game game = Game.builder().id(id)
                    .status(GameStatus.CREATED)
                    .players(playerList)
                    .cardPerPlayer(cardPerPlayer)
                    .deck(deck).build();
             repository.save(game);
             LinkedList list = new LinkedList();
             list.add(GameEvent.GAME_CREATED);
             listener.notifyGameEvent(id, list);
             return game;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game updateGame(GameDTO gameDTO, UUID id) {
        try {

            LinkedList listenerList = new LinkedList();
            Game game = repository.findById(id);

            //player can be removed if he decides to quit during the game
            if (gameDTO.getGameEvent().contains(GameEvent.PLAYER_REMOVED) && game.getStatus().equals(GameStatus.STARTED)) {
                game = removePlayerDuringTheGame(gameDTO, game);
                listenerList.add(GameEvent.PLAYER_REMOVED);
                listener.notifyGameEvent(id, listenerList);
                return game;
            }

            if (allowedGameStatusToUpdate.contains(game.getStatus())) {
                if (gameDTO.getPlayers() != null && !gameDTO.getPlayers().isEmpty()) {
                    List<Player> players = gameDTO.getPlayers().stream()
                            .map(p -> Player.builder().id(p.getId()).build()).collect(Collectors.toList());
                    game.setPlayers(players);
                    listenerList.add(GameEvent.PLAYER_UPDATED);
                } else {
                    validateDeck(game.getDeck(), gameDTO.getDeck());
                    game.setDeck(gameDTO.getDeck());
                    listenerList.add(GameEvent.DECK_ADDED);
                }

                int number_of_card_to_player = gameDTO.getNumberOfCardsPerPlayer() == null ?
                        game.getCardPerPlayer() : gameDTO.getNumberOfCardsPerPlayer();
                game.setCardPerPlayer(number_of_card_to_player);
                repository.update(game);
                listener.notifyGameEvent(id, listenerList);
                return game;
            } else {
                throw new GameUpdateException("Update failed since the Game is" + game.getStatus());
            }
        } catch (GameNotFoundException e) {
            throw new GameNotFoundException(e.getMessage());
        }
    }


    @Override
    public Game dealCards(UUID id) {
        try {
            LinkedList listenerList = new LinkedList();
            Game game = repository.findById(id);
            Stack<Card> shuffledDeck = cardService.createDeck(game.getDeck());
            listenerList.add(GameEvent.CARD_SHUFFLED);
            game.getPlayers().stream()
                    .map(player -> assignCardToPlayer(shuffledDeck, player, game.getCardPerPlayer(), game.getId()))
                    .collect(Collectors.toList());
            game.setUnDealtCards(shuffledDeck);
            game.setStatus(GameStatus.STARTED);
            listenerList.add(GameEvent.CARD_DEALT);
            repository.save(game);
            listener.notifyGameEvent(id, listenerList);
            return game;
        } catch (GameNotFoundException e) {
            throw new GameNotFoundException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());

        }
    }

    @Override
    public Game getGameById(UUID id, String order, String sortField) {
        try {
            Game game = repository.findById(id);
            if (game.getPlayers() != null && !game.getPlayers().isEmpty()) {
                sort(game.getPlayers(), order, sortField);
            }
            return game;
        } catch (GameNotFoundException e) {
            throw new GameNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteGame(UUID id) {
        LinkedList listenerList = new LinkedList();
        try {
            repository.delete(id);
        } catch(RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        listenerList.add(GameEvent.GAME_DELETED);
        listener.notifyGameEvent(id, listenerList);
    }


    //TODO: write the code in a round robin to deal cards to players in the list

    private Player assignCardToPlayer(Stack<Card> card, Player player, Integer cardPerPlayer, UUID gameId) {
        List<Card> cardList = new ArrayList<>();
        GameInfo gameInfo = GameInfo.builder().gameId(gameId).build();
        for (int i = 1; i <= cardPerPlayer; i++) {
            try {
                Card topCard = card.pop();
                cardList.add(topCard);
            } catch (EmptyStackException e) {
                // no cards left in the deck
            }
        }
        gameInfo.setCardList(cardList);
        List<GameInfo> gameInfoList = List.of(gameInfo);
        player.setGameInfos(gameInfoList);
        return player;
    }


    private void validateDeck(Integer savedDeck, Integer deckToUpdate) {
        if (deckToUpdate != null && deckToUpdate < savedDeck) {
            throw new GameUpdateException("A deck cannot be removed once added to the game");
        }
    }


    // code to be refactored to handle dynamic field for sorting. Right now only faceValue is sorted
    private void sort(List<Player> player, String order, String field) {
        if (order.equals("desc") || order == null) {
            player.sort(Comparator.comparingInt(this::calculateTotalFaceValue).reversed());
        } else {
            player.sort(Comparator.comparingInt(this::calculateTotalFaceValue));
        }
    }

    private int calculateTotalFaceValue(Player player) {
        if (player.getGameInfos() != null && player.getGameInfos().get(0).getCardList() != null) {
            return player.getGameInfos().stream()
                    .flatMap(gameInfo -> gameInfo.getCardList().stream())
                    .mapToInt(card -> card.getValue().ordinal())
                    .sum();
        }
        return 0;
    }


    private Game removePlayerDuringTheGame(GameDTO gameDTO, Game game) {
        if(gameDTO.getGameEvent().contains(GameEvent.PLAYER_REMOVED) && game.getStatus().equals(GameStatus.STARTED)) {
            List<UUID> playerToRemove = gameDTO.getPlayers().stream().map(player -> player.getId()).collect(Collectors.toList());
            List<Player> playerList = game.getPlayers().stream().filter(player -> !playerToRemove.contains(player.getId())).collect(Collectors.toList());
            game.setPlayers(playerList);
            repository.update(game);
            return game;
        }
        return game;

    }

}


