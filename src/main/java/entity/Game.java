package entity;

import enums.GameStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Stack;
import java.util.UUID;


@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Game {

    private UUID id;

    private List<Player> players;

    private Integer cardPerPlayer;

    private Integer deck;

    private GameStatus status;

    private Integer potSize;

    private Stack<Card> unDealtCards;

}
