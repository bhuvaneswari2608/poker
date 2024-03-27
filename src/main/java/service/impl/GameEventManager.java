package service.impl;

import enums.GameEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import repository.GameEventRepository;
import service.GameEventListener;

import java.util.LinkedList;
import java.util.UUID;

@Service
public class GameEventManager implements GameEventListener {

    @Autowired
    private GameEventRepository repo;

    @Override
    @Async
    public void notifyGameEvent(UUID id, LinkedList<GameEvent> gameEvent) {
        repo.saveEvent(id, gameEvent);
    }

    @Override
    public LinkedList getEvents(UUID gameId) {
       return repo.getEvents(gameId);
    }
}
