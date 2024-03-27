package entity;

import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import enums.PlayerRole;
import enums.PlayerStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.util.List;
import java.util.UUID;


@Builder
@Setter
@Getter
@EqualsAndHashCode
public class GameInfo {

    private List<Card> cardList;

    private PlayerStatus status;

    private PlayerRole role;

    private Integer chipCount;

    private UUID gameId;


}
