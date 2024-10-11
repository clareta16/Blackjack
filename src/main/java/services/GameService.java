package services;

import cat.itacademy.s05.t01.n01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.exception.PlayerNotFoundException;
import model.Game;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.GameInterface;


@Service
public class GameService {

    @Autowired
    private GameInterface.GameRepository gameRepository;

    public Mono<Game> createGame(String playerUsername) {
        Game game = new Game();
        game.setPlayerUsername(playerUsername);
        return gameRepository.save(game);
    }

    public Mono<Game> getGameDetails(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + id + " not found")));

    }

    public Mono<String> playGame(String id, String action, int bet) {
        return Mono.just("Action's result");
    }

    public Mono<Void> deleteGame(String id) {
        return gameRepository.deleteById(id);
    }

    public Flux<String> getRanking() {
        return Flux.just("Ranking");
    }

    public Mono<Player> changePlayerName(String playerId, String newUsername) {
        return Mono.just(new Player(playerId, newUsername));
    }
}
