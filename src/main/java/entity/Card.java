package entity;

import enums.FaceValue;
import enums.Suits;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Card {
    private FaceValue value;

    private Suits suits;

    public void setValue(FaceValue value) {
        this.value = value;
    }

    public  Suits getSuits() {
        return suits;
    }

    public void setSuits(Suits suits) {
        this.suits = suits;
    }

    public  FaceValue getValue() {
        return value;
    }


}
