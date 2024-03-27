package entity;

import enums.FaceValue;
import enums.Suits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Deck {
    private static List<Card> cardList;

    static {
        cardList = new ArrayList<>();
        for(Suits suits : Suits.values()) {
            List<Card> cards = Arrays.stream(FaceValue.values())
                    .map(value -> Card.builder().value(value).suits(suits).build()).toList();
            Stream.generate(() -> cards).limit(2).collect(Collectors.toList());
            cardList.addAll(cards);
        }

    }

}
