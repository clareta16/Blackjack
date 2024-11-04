package cat.itacademy.s05.t01.n01.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<Card> cards = new ArrayList<>();
    private static final int STAND_THRESHOLD = 17;

    public Dealer() {}

    public List<Card> getCards() {
        return cards;
    }

    public void playTurn(Deck deck) {
        while (getCardsValue() < STAND_THRESHOLD) {
            drawCard(deck);
        }
    }

    private void drawCard(Deck deck) {
        if (deck.isEmpty()) {
            throw new IllegalStateException("The deck is empty. Cannot draw a card.");
        }
        cards.add(deck.draw());
    }

    public int getCardsValue() {
        int cardsValue = 0;
        int acesCount = 0;

        for (Card card : cards) {
            cardsValue += card.getValue();
            if (card.getRank().equals("A")) {
                acesCount++;
            }
        }
        while (cardsValue > 21 && acesCount > 0) {
            cardsValue -= 10;
            acesCount--;
        }
        return cardsValue;
    }
}
