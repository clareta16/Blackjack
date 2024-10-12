package services;

import cat.itacademy.s05.t01.n01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.exception.PlayerNotFoundException;
import model.Game;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.GameRepository;
import repository.PlayerRepository;

import java.util.List;


@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Mono<Game> createGame(List<Player> players) {
        Game game = new Game();

        players.forEach(player -> game.addPlayer(player));
        return gameRepository.save(game);
    }

    public Mono<Game> startGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found")))
                .flatMap(game -> {
                    try {
                        game.startGame();
                    } catch (IllegalStateException e) {
                        return Mono.error(e);
                    }
                    return gameRepository.save(game)
                            .thenReturn(game);
                });
    }

    public Mono<Game> getGameDetails(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + id + " not found")));

    }

    public Mono<Player> playerWantsCard(String gameId, String playerId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + gameId + " not found")))
                .flatMap(game -> {
                    Player player = null;
                    try {
                        player = game.getPlayers().stream()
                                .filter(p -> p.getId().equals(playerId))
                                .findFirst()
                                .orElseThrow(() -> new PlayerNotFoundException("Player with ID " + playerId + " not found in this game"));
                    } catch (PlayerNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    game.dealCardToPlayer(player);

                    return gameRepository.save(game)
                            .thenReturn(player);
                });
    }


    public Mono<Void> playerStopsDrawing(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found")))
                .flatMap(game -> {
                    game.playerStopsDrawing(gameId);
                    return gameRepository.save(game).then();
                });
    }

    public Mono<Void> dealerTurn(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found")))
                .flatMap(game -> {
                    game.getDealer().playTurn(game.getDeck());
                    return gameRepository.save(game).then();
                });
    }

    public Mono<List<Player>> determineWinners(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found")))
                .map(game -> {
                    List<Player> winners = game.determineWinners();
                    winners.forEach(playerRepository::save);
                    return winners;
                });
    }

    public Mono<Void> deleteGame(String id) {
        return gameRepository.deleteById(id);
    }

    public Flux<String> getRanking() {
        return Flux.just("Ranking");
    }

}
