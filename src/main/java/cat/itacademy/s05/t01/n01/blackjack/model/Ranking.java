package cat.itacademy.s05.t01.n01.blackjack.model;

public class Ranking {
    private String id;
    private int wins;

    public Ranking(String id, int wins) {
        this.id = id;
        this.wins = wins;
    }

    public String getId() {
        return id;
    }

    public int getWins() {
        return wins;
    }
}

