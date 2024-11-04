package cat.itacademy.s05.t01.n01.blackjack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.List;
import java.util.ArrayList;


@Table("players")
public class Player {

    @Id
    private String id;
    private String username;
    private boolean isPlaying;
    private int wins;
    private int totalCardsValue;

    public Player(String username) {
    this.username = username;
    this.totalCardsValue = 0;
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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}


