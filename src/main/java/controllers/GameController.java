package controllers;

import model.Game;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import services.GameService;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestBody List<Player> playerUsernames) {
        return gameService.createGame(playerUsernames)
                .map(game -> ResponseEntity.status(201).body(game))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/games/{id}/start")
    public Mono<ResponseEntity<Game>> startGame(@PathVariable String id) {
        return gameService.startGame(id)
                .map(game -> ResponseEntity.ok(game))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/game/{id}")
    public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id)
                .map(game -> ResponseEntity.ok(game));
    }

    @PostMapping("/game/{id}/play")
    public Mono<ResponseEntity<Player>> playerWantsCard(@PathVariable String playerId, @RequestParam String gameId) {
        return gameService.playerWantsCard(gameId, playerId)
                .map(player -> ResponseEntity.ok(player))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/{gameId}/player/stops-drawing")
    public Mono<ResponseEntity<Object>> playerStopsDrawing(@PathVariable String gameId) {
        return gameService.playerStopsDrawing(gameId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/{gameId}/dealer/turn")
    public Mono<ResponseEntity<Object>> dealerTurn(@PathVariable String gameId) {
        return gameService.dealerTurn(gameId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/game/{id}/delete")
    public Mono<ResponseEntity<Object>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .map(aVoid -> ResponseEntity.noContent().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //a l'utilitzar notFound o noContent s'ha de posar build per acabar de crear la response entity

    @GetMapping("/ranking")
    public Flux<ResponseEntity<String>> getRanking() {
        return gameService.getRanking()
                .map(ranking -> ResponseEntity.ok(ranking));
    }

    @GetMapping("/{gameId}/winners")
    public Mono<ResponseEntity<List<Player>>> determineWinners(@PathVariable String gameId) {
        return gameService.determineWinners(gameId)
                .map(winners -> ResponseEntity.ok(winners))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}

