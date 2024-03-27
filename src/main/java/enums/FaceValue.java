package enums;

import java.util.Arrays;
import java.util.Optional;

public enum FaceValue {
    ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
    SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(11), QUEEN(12), KING(13);

    public int value;

    FaceValue(int value) {
        this.value = value;
    }

    public static FaceValue getFaceValue(int value) {
        return Arrays.stream(FaceValue.values()).filter(fv -> fv.value == value)
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }


}
