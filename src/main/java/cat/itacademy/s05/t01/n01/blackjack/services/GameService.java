package cat.itacademy.s05.t01.n01.blackjack.services;

import cat.itacademy.s05.t01.n01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.model.Deck;
import cat.itacademy.s05.t01.n01.blackjack.model.Game;
import cat.itacademy.s05.t01.n01.blackjack.model.Player;
import cat.itacademy.s05.t01.n01.blackjack.model.Ranking;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.blackjack.repository.GameRepository;
import cat.itacademy.s05.t01.n01.blackjack.repository.PlayerRepository;

import java.util.Comparator;

@Service
public class GameService {
    private Deck deck;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public GameService() {
        this.deck = new Deck();
    }

    public Mono<Game> createGame(String playerId) {
        if (playerId == null || playerId.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Player ID can't be null or empty"));
        }

        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player with ID " + playerId + " not found")))
                .flatMap(savedPlayer -> {
                    Game game = new Game(savedPlayer);
                    return gameRepository.save(game);
                });
    }

    public Mono<Game> getGameDetails(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + id + " not found")));
    }

    public Mono<Game> playGame(String gameId, String moveType) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + gameId + " not found")))
                .flatMap(game -> {
                    if ("hit".equalsIgnoreCase(moveType)) {
                        game.dealCardToPlayer();
                        int playerScore = game.getPlayerCardsValue();

                        if (playerScore > 21) {
                            game.setResult("Player loses!");
                            game.setActive(false);
                        }
                    } else if ("stand".equalsIgnoreCase(moveType)) {
                        game.playerStopsDrawing();
                        int playerScore = game.getPlayerCardsValue();
                        int dealerScore = game.getDealer().getCardsValue();

                        if (playerScore > 21) {
                            game.setResult("Player loses!");
                        } else if (dealerScore > 21 || playerScore > dealerScore) {
                            game.setResult("Player wins!");
                            game.getPlayer().setWins(game.getPlayer().getWins() + 1);
                        } else if (playerScore < dealerScore) {
                            game.setResult("Dealer wins!");
                        } else {
                            game.setResult("It's a tie!");
                        }
                    } else {
                        return Mono.error(new IllegalArgumentException("Invalid move type: " + moveType));
                    }

                    return playerRepository.save(game.getPlayer())
                            .then(gameRepository.save(game));
                });
    }

    public Flux<Ranking> getAllPlayerRankings() {
        return playerRepository.findAll()
                .map(player -> new Ranking(player.getUsername(), player.getWins()))
                .sort(Comparator.comparingInt(Ranking::getWins).reversed());
    }
}

