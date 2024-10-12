package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scores")
public class Score {
    @Id
    private String id;
    private String username;
    private String userId;
    private int wins = 0;
    private int losses = 0;
    private int moneyWon = 0;

    public Score(String id, String username, String userId, int wins, int losses, int moneyWon) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.wins = wins;
        this.losses = losses;
        this.moneyWon = moneyWon;
    }

    public Score(String id, String username) {
        this.username = username;
        this.id = id;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getMoneyWon() {
        return moneyWon;
    }

    public void addWin() {
        this.wins += 1;
    }

    public void addLosses(){
        this.losses += 1;
    }

    public void addMoneyWon(Player player) {
       this.moneyWon += player.getBet();
    }


}
