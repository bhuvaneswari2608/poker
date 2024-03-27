package repository;

import enums.GameEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameEventRepository {
    private final LinkedHashMap<UUID, LinkedList<GameEvent>> map = new LinkedHashMap<>();

    public synchronized void saveEvent(UUID id, LinkedList<GameEvent> gameEvent) {
        LinkedList list = map.get(id);
        if(list == null || list.isEmpty() ) {
            LinkedList ll = new LinkedList<>();
            ll.addAll(gameEvent);
            map.put(id, ll);
        } else {
            list.addAll(gameEvent);
        }
    }


    public LinkedList getEvents(UUID uuid) {
        LinkedList list = map.get(uuid);
        if(list == null || list.isEmpty() ) {
           return new LinkedList();
        }
        return list;
    }

}
