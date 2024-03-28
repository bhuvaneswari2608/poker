package DTO;

import entity.Card;
import enums.PlayerRole;
import enums.PlayerStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class PlayerDTO {

    private UUID id;

    private List<CardDTO> cardList;

    private PlayerStatus status;

    private PlayerRole role;

}
