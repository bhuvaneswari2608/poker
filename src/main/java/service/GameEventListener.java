package service;

import enums.GameEvent;

import java.util.LinkedList;
import java.util.UUID;

public interface GameEventListener {
    void notifyGameEvent(UUID id, LinkedList<GameEvent> gameEvent);

    LinkedList getEvents(UUID gameId);
}
