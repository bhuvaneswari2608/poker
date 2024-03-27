package mapper;

import DTO.CardDTO;
import DTO.GameDTO;
import DTO.PlayerDTO;
import entity.Card;
import entity.Game;
import entity.GameInfo;
import entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameMapper {

    public static GameDTO mapGame(Game game) {
        List<PlayerDTO> playerDTOS = game.getPlayers().stream()
                .map(player -> buildPlayerDTO(player))
                .collect(Collectors.toList());
        return GameDTO.builder()
                .uuid(game.getId())
                .gameStatus(game.getStatus())
                .players(playerDTOS)
                .gameStatus(game.getStatus())
                .deck(game.getDeck())
                .unDealtCards(buildCardDTO(game.getUnDealtCards()))
                .build();
    }

    private static PlayerDTO buildPlayerDTO(Player player) {
        //For a given game the list is one, since the data is retrieved from game Repository
        PlayerDTO playerDTO = PlayerDTO.builder().id(player.getId())
                .build();
        if(player.getGameInfos() != null) {
            GameInfo gameInfo = player.getGameInfos().get(0);
            playerDTO.setCardList(gameInfo.getCardList());

        }

        return playerDTO;
    }

    private static List<CardDTO> buildCardDTO(List<Card> cards) {
        if (cards == null) {
            return new ArrayList<>();
        }
        List<CardDTO> cardDTOS = cards.stream().map(card -> CardDTO.builder()
                        .suits(card.getSuits())
                        .faceValue(card.getValue()).build())
                .collect(Collectors.toList());

        return cardDTOS;
    }

}
