package DTO;


import enums.GameEvent;
import enums.GameStatus;
import jakarta.validation.constraints.NotNull;
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
    private List<CardDTO> unDealtCards;
    @NotNull(groups = UpdateValidation.class)
    private List<GameEvent> gameEvent;


}
