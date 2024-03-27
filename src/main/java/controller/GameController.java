package controller;

import DTO.GameDTO;
import entity.Game;
import mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.GameService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO gameDTO){
        Game game = gameService.createGame(gameDTO);
        return new ResponseEntity<>(GameMapper.mapGame(game), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<GameDTO> updateGame(@PathVariable("id") UUID id, @RequestBody GameDTO gameDTO ) {
        Game game = gameService.updateGame(gameDTO, id);
        return new ResponseEntity<>(GameMapper.mapGame(game), HttpStatus.OK);
    }

    @PatchMapping("/{id}/dealCards")
    public ResponseEntity<GameDTO>  dealCards(@PathVariable("id") UUID id) {
        Game game = gameService.dealCards(id);
        return new ResponseEntity<>(GameMapper.mapGame(game), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable("id") UUID id, @RequestParam(defaultValue = "desc") String sortOrder,
                                           @RequestParam(defaultValue = "card") String sortField) {
        return new ResponseEntity<>(GameMapper.mapGame(gameService.getGameById(id, sortOrder, sortField)), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGame(@PathVariable("id") UUID id) {
        gameService.deleteGame(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
