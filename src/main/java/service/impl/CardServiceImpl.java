package service.impl;

import entity.Card;
import enums.FaceValue;
import enums.Suits;
import org.springframework.stereotype.Service;
import service.CardService;

import java.util.*;
import java.util.stream.Stream;

@Service
public class CardServiceImpl implements CardService {

    private static HashMap<Integer, List<Card>> cardsDealt = new HashMap<>();
    static {
        cardsDealt.put(1, Collections.emptyList());
    }
    @Override
    public Card dealCard() {
        Card card = getCard();
int count = 0 ;
        if(count== 0) {
            card = dealCard();
            count = 1;
        }
        cardsDealt.put(1, Arrays.asList(card));
        return card;
    }

    @Override
    public Stack<Card> createDeck(int numOfDeck) {
        List<Card> cardList = new ArrayList<>();
        for (Suits suits : Suits.values()) {
            List<Card> cards = Arrays.stream(FaceValue.values())
                    .flatMap(value -> Stream.generate(() -> Card.builder().value(value).suits(suits).build())
                            .limit(numOfDeck)).toList();
            cardList.addAll(cards);
        }
        return shuffleCards(cardList);
    }

    @Override
    public Stack<Card> shuffleCards(List<Card> cards){
        Collections.shuffle(cards);
        Stack<Card> shuffledCards = new Stack<>();
        shuffledCards.addAll(cards);
        return shuffledCards;
    }


    private Card getCard() {
        Suits[] suits = Suits.getSuitArray();
        Random random = new Random();
        int selectedSuit = random.nextInt(suits.length);
        int selectedFaceValue = random.nextInt(FaceValue.values().length);


       return  Card.builder().value(FaceValue.getFaceValue(selectedFaceValue))
                .suits(suits[selectedSuit]).build();
    }


    private boolean checkIfPresent(Card card) {
        return cardsDealt.get(1).stream().anyMatch(c ->c.getSuits().equals(card.getSuits()) && c.getValue().equals(card.getValue()));

    }
}
