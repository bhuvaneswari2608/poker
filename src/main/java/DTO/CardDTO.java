package DTO;

import enums.FaceValue;
import enums.Suits;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CardDTO {
    private FaceValue faceValue;
    private Suits suits;
}
