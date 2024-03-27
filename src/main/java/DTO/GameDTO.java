package DTO;

import enums.GameStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class GameDTO {

    private List<PlayerDTO> players;
    private Integer deck;
    private Integer numberOfCardsPerPlayer;
    private Integer numberOfCommunityCards;
    private List<CardDTO> communityCardList;
    private GameStatus gameStatus;
    private UUID uuid;
    private GameUpdateEvent gameUpdateEvent;


}
