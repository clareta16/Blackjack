package controllers;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import services.PlayerService;
import org.springframework.http.ResponseEntity;

@RestController //Controller + Response body
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/new")
    public Mono<ResponseEntity<Player>> createPlayer(@RequestParam String id, String username) {
        return playerService.createPlayer(username)
                .map(player -> ResponseEntity.status(201).body(player))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Player>> getPlayerDetails(@PathVariable String id) {
        return playerService.getPlayerDetails(id)
                .map(player -> ResponseEntity.ok(player));
    }

    @PutMapping("/player/{playerId}")
    public Mono<ResponseEntity<Player>> changePlayerUsername(@PathVariable String playerId, @RequestBody String newUsername) {
        return playerService.changePlayerUsername(playerId, newUsername)
                .map(player -> ResponseEntity.ok(player))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public Flux<ResponseEntity<Player>> getAllPlayers() {
        return playerService.getAllPlayers()
                .map(player -> ResponseEntity.ok(player));
    }

    @PostMapping("/{playerId}/bet")
    public Mono<ResponseEntity<Player>> placeBet(
            @PathVariable String playerId,
            @RequestParam String gameId,
            @RequestParam int amount) {

        return playerService.placeBet(gameId, playerId, amount)
                .map(player -> ResponseEntity.ok(player)) // Return success response
                .onErrorReturn(ResponseEntity.badRequest().build()); // Handle error response
    }
}
