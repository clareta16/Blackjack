package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table("players")
public class Player {

    @Id
    private String id;
    private String username;
    private int bet;
    private boolean isPlaying;
    private List<Card> cards = new ArrayList<>();

    public Player(String username) {
        this.username = username;
    }

    public Player(String id, String username, int bet, List<Card> cards) {
        this.cards = cards;
        this.id = id;
        this.username = username;
        this.bet = bet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
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

    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
        } else {
            throw new IllegalArgumentException("Card cannot be null");
        }
    }
}

