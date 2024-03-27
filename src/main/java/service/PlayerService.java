package service;

import DTO.PlayerDTO;
import entity.Player;

public interface PlayerService {
    Player save(PlayerDTO playerDTO);
}
