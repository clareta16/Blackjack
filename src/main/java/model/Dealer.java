package model;

import java.util.List;
import java.util.Scanner;

public class Dealer {
    private List<Card> cards;

    public Dealer(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getCardsValue() {
        int cardsValue = 0;

        for (Card card : cards) {
            cardsValue += card.getValue();
            if (card.getRank().equals("A")) {
                if (cardsValue <= 10) {
                    cardsValue += 11;
                } else {
                    cardsValue += 1;
                }
            }

        }
        return cardsValue;
    }
}

