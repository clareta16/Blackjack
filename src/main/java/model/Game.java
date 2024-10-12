package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "games")
public class Game {
    @Id
    private String id;
    private List<Player> players = new ArrayList<>();
    private Deck deck;
    private Score score;
    private Dealer dealer;
    private int currentPlayerIndex;
    private boolean isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Score getScore() {
        return score;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void startGame() {
        if (players.isEmpty()) {
            throw new IllegalStateException("Oh no! It looks like there are no players ready yet.");
        }
        this.deck = new Deck();
        this.deck.shuffle();
        this.dealer = new Dealer();
        this.isActive = true;
        this.currentPlayerIndex = 0;

        for (Player player : players) {
            player.setCards(List.of(deck.draw(), deck.draw()));
            player.setPlaying(true);
        }
    }

    public void dealCardToPlayer(Player player) {
        if (deck.isEmpty()) {
            throw new IllegalStateException("The deck is empty. There are no more cards to deal.");
        }
        Card dealtCard = deck.draw();
        player.addCard(dealtCard);
    }

    public void playerStopsDrawing(String playerId) {
        Player player = players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        player.setPlaying(false);
    }

    public List<Player> determineWinners() {
        List<Player> winners = new ArrayList<>();
        int dealerValue = dealer.getCardsValue();

        for (Player player : players) {
            int playerValue = player.getCardsValue();
            if (playerValue > 21) {
                continue;
            }
            if (playerValue > dealerValue || dealerValue > 21) {
                winners.add(player);
            }
        }
        if (winners.isEmpty() && dealerValue <= 21) {
            System.out.println("Dealer wins!");
        } else {
            System.out.println("Winners: " + winners);
        }

        return winners;
    }
}


