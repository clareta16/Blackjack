package services;

import cat.itacademy.s05.t01.n01.blackjack.exception.PlayerNotFoundException;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import repository.PlayerRepository;
import reactor.core.publisher.Flux;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

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
}


