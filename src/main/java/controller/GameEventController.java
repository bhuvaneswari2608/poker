package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.GameEventListener;

import java.util.LinkedList;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gameEvent")

public class GameEventController {

    @Autowired
    private GameEventListener gameEventListener;

    @GetMapping("/{gameId}")
    public ResponseEntity<LinkedList> getGame(@PathVariable("gameId") UUID gameId) {
        return new ResponseEntity<>(gameEventListener.getEvents(gameId), HttpStatus.OK);

    }
}
