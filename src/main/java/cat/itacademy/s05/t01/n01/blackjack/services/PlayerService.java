package cat.itacademy.s05.t01.n01.blackjack.services;

import cat.itacademy.s05.t01.n01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.blackjack.repository.GameRepository;
import cat.itacademy.s05.t01.n01.blackjack.repository.PlayerRepository;
import reactor.core.publisher.Flux;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    public Mono<Player> createPlayer(String username) {
        Player player = new Player(username);
        return playerRepository.save(player);
    }

    public Mono<Player> getPlayerDetails(String id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player with ID " + id + " not found")));
    }

    public Mono<Player> changePlayerUsername(String playerId, String newUsername) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.setUsername(newUsername);
                    return playerRepository.save(player);
                })
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player with ID " + playerId + " not found")));
        // si el flatmap busca el jugador i no existeix, llença l'excepció
    }

    public Flux<Player> getAllPlayers() {
        return playerRepository.findAll();
    }


    public Mono<Player> placeBet(String id, String playerId, int amount) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found")))
                .flatMap(game -> {
                    Player player = null;
                    try {
                        player = game.getPlayers().stream()
                                .filter(p -> p.getId().equals(playerId))
                                .findFirst()
                                .orElseThrow(() -> new GameNotFoundException("Player not found with id: " + playerId));
                    } catch (GameNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    player.setBet(amount);

                    return gameRepository.save(game)
                            .thenReturn(player);
                });
    }
}


