package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dealer {
    private Deck deck;
    private List<Card> cards = new ArrayList<>();
    private static final int STAND_THRESHOLD = 17;

    public Dealer(Deck deck) {
        this.deck = deck;
    }

    public Dealer() {}

    public List<Card> getCards() {
        return cards;
    }

    public void playTurn(Deck deck) {
        cards.clear();

        while (getCardsValue() < STAND_THRESHOLD) {
            drawCard();
        }
    }

    private void drawCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("The deck is empty. Cannot draw a card.");
        }
        Card card = deck.draw();
        cards.add(card);
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

