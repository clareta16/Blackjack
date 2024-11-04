package cat.itacademy.s05.t01.n01.blackjack.model;

import java.util.Collections;
import java.util.Stack;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<Card> deckCards;

    public Deck() {
        deckCards = new Stack<>();
        //amb Stack només pots accedr a la carta superior
        String[] suits = {"Hearts", "Clubs", "Spades", "Diamonds"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                deckCards.add(new Card(suit, ranks[i], values[i]));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(deckCards);
    }

    public boolean isEmpty() {
        return deckCards.isEmpty();
    }

    public Card draw() {
        if (deckCards.isEmpty()) {
            throw new IllegalStateException("The deck is empty. Cannot draw a card.");
        }
        return deckCards.pop();
        //elimina i torna l'última carta
    }
}

