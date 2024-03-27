package entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
public class Player {

    private UUID id;

    private List<GameInfo> gameInfos;
}



