package service;

import entity.Card;

import java.util.List;
import java.util.Stack;

public interface CardService {
    Card dealCard();

    Stack<Card> createDeck(int numOfDeck);

    Stack<Card> shuffleCards(List<Card> cards);
}
