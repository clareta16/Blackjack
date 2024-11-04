package cat.itacademy.s05.t01.n01.blackjack.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Game entity representing each game")
@Document(collection = "game")
public class Game {
    @Id
    private String id;
    private Player player;
    private Deck deck;
    private Dealer dealer;
    private List<Card> playerCards;
    private List<Card> dealerCards;
    private boolean isActive;
    private String result;


    public Game(Player player) {
        this.player = player;
        this.deck = new Deck();
        this.dealer = new Dealer();
        this.playerCards = new ArrayList<>();
        this.dealerCards = new ArrayList<>();
        this.isActive = true;

        startGame();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public Deck getDeck() {
        return deck;
    }
    public Dealer getDealer() {
        return dealer;
    }
    public void setActive(boolean active) {
        isActive = active;
    }

    public void startGame() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("The deck is empty.");
        }

        player.setPlaying(true);
        result = null;
        isActive = true;

        playerCards.add(deck.draw());
        playerCards.add(deck.draw());
        dealerCards.add(deck.draw());
        dealerCards.add(deck.draw());
    }

    public void dealCardToPlayer() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("The deck is empty. No more cards can be dealt.");
        }
        Card drawnCard = deck.draw();
        playerCards.add(drawnCard);
        player.addCard(drawnCard);
    }

    public void playerStopsDrawing() {
        player.setPlaying(false);
        dealer.playTurn(deck);
        dealerCards = dealer.getCards();
    }

    public int getPlayerCardsValue() {
        int totalValue = 0;
        int acesCount = 0;
        for (Card card : playerCards) {
            totalValue += card.getValue();
            if (card.getRank().equals("A")) {
                acesCount++;
            }
        }
        while (totalValue > 21 && acesCount > 0) {
            totalValue -= 10;
            acesCount--;
        }
        return totalValue;
    }
}










