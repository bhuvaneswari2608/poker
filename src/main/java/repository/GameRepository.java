package repository;

import entity.Game;
import exception.GameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Component
public class GameRepository {

    private static final HashMap<UUID, Game> db = new HashMap<>();

    public static synchronized Game save(Game game) {
        db.put(game.getId(), game);
        return game;
    }


    public static synchronized void  delete(UUID game) {
        db.remove(game);
    }

    public Game update(Game game) {
        return save(game);
    }

    public Game findById(UUID id) throws GameNotFoundException {
        try {
            return db.get(id);
        } catch (NullPointerException e) {
            throw new GameNotFoundException("Game doesnt exist for the given "+ id);
        }
    }
}
