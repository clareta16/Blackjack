package controllers;

import model.Game;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import services.GameService;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/game/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestBody String playerUsername) {
        return gameService.createGame(playerUsername)
                .map(game -> ResponseEntity.status(201).body(game))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/game/{id}")
    public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id)
                .map(game -> ResponseEntity.ok(game));
    }

    @PostMapping("/game/{id}/play")
    public Mono<ResponseEntity<String>> playGame(@PathVariable String id, @RequestBody String action, int bet) {
        return gameService.playGame(id, action, bet)
                .map(result -> ResponseEntity.ok(result))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/game/{id}/delete")
    public Mono<ResponseEntity<Object>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .map(aVoid -> ResponseEntity.noContent().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/ranking")
    public Flux<ResponseEntity<String>> getRanking() {
        return gameService.getRanking()
                .map(ranking -> ResponseEntity.ok(ranking));
    }
}
