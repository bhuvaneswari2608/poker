package DTO;

import enums.FaceValue;
import enums.Suits;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CardDTO {
    private FaceValue faceValue;
    private Suits suits;
}
