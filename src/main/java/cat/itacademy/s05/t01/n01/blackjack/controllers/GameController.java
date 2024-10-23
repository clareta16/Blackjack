package cat.itacademy.s05.t01.n01.blackjack.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import cat.itacademy.s05.t01.n01.blackjack.model.Game;
import cat.itacademy.s05.t01.n01.blackjack.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.blackjack.services.GameService;

import java.util.List;

@RestController
@RequestMapping("/game")
@Tag(name = "Game Management System", description = "Operations pertaining to Blackjack games")
public class GameController {


    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    @Operation(summary = "Create a new game", description = "Create a new Blackjack game with the specified players.")
    @ApiResponse(responseCode = "201", description = "Game created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")

    public Mono<ResponseEntity<Game>> createGame(@RequestBody List<Player> playerUsernames) {
        return gameService.createGame(playerUsernames)
                .map(game -> ResponseEntity.status(201).body(game))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start the game", description = "Start a Blackjack game by game ID.")

            @ApiResponse(responseCode = "200", description = "Game started successfully")
            @ApiResponse(responseCode = "404", description = "Game not found")

    public Mono<ResponseEntity<Game>> startGame(@PathVariable String id) {
        return gameService.startGame(id)
                .map(game -> ResponseEntity.ok(game))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get game details", description = "Retrieve the details of a specific game by its ID.")
            @ApiResponse(responseCode = "200", description = "Game found")
            @ApiResponse(responseCode = "404", description = "Game not found")

    public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id)
                .map(game -> ResponseEntity.ok(game));
    }

    @PostMapping("/{id}/play")
    @Operation(summary = "Player draws a card", description = "Player draws a card during their turn in the game.")
            @ApiResponse(responseCode = "200", description = "Card drawn successfully")
            @ApiResponse(responseCode = "400", description = "Invalid game or player ID")

    public Mono<ResponseEntity<Player>> playerWantsCard(@PathVariable String playerId, @RequestParam String gameId) {
        return gameService.playerWantsCard(gameId, playerId)
                .map(player -> ResponseEntity.ok(player))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/{gameId}/player/stops-drawing")
    @Operation(summary = "Player stops drawing cards", description = "Player decides to stop drawing cards in the game.")
            @ApiResponse(responseCode = "200", description = "Player stopped drawing cards")
            @ApiResponse(responseCode = "400", description = "Invalid game ID")

    public Mono<ResponseEntity<Object>> playerStopsDrawing(@PathVariable String gameId) {
        return gameService.playerStopsDrawing(gameId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/{gameId}/dealer/turn")
    @Operation(summary = "Dealer's turn", description = "Dealer plays their turn in the Blackjack game.")
    @ApiResponse(responseCode = "200", description = "Dealer's turn completed")
    @ApiResponse(responseCode = "400", description = "Invalid game ID")

    public Mono<ResponseEntity<Object>> dealerTurn(@PathVariable String gameId) {
        return gameService.dealerTurn(gameId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete game", description = "Deletes a game by its ID.")
    @ApiResponse(responseCode = "204", description = "Game deleted successfully")
    @ApiResponse(responseCode = "404", description = "Game not found")

    public Mono<ResponseEntity<Object>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .map(aVoid -> ResponseEntity.noContent().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/ranking")
    @Operation(summary = "Get game ranking", description = "Retrieve the ranking of players in all games.")
    @ApiResponse(responseCode = "200", description = "Ranking retrieved successfully")

    public Flux<ResponseEntity<String>> getRanking() {
        return gameService.getRanking()
                .map(ranking -> ResponseEntity.ok(ranking));
    }

    @GetMapping("/{gameId}/winners")
    @Operation(summary = "Determine winners", description = "Determine the winners of a game by its ID.")
    @ApiResponse(responseCode = "200", description = "Winners determined successfully")
    @ApiResponse(responseCode = "400", description = "Invalid game ID")

    public Mono<ResponseEntity<List<Player>>> determineWinners(@PathVariable String gameId) {
        return gameService.determineWinners(gameId)
                .map(winners -> ResponseEntity.ok(winners))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("L'aplicació està funcionant");
    }


}


