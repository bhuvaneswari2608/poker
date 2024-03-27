package service;

import DTO.GameDTO;
import entity.Game;
import exception.GameNotFoundException;

import java.util.UUID;

public interface GameService {

    Game createGame(GameDTO game);

    Game updateGame(GameDTO gameDTO, UUID id) throws GameNotFoundException;

    Game dealCards(UUID id);

    Game getGameById(UUID id, String order, String sortField);

    void deleteGame(UUID id);


}
