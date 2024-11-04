package cat.itacademy.s05.t01.n01.blackjack.controllers;

import cat.itacademy.s05.t01.n01.blackjack.model.Ranking;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.blackjack.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/game")
@Tag(name = "Game Management", description = "Operations related to managing Blackjack")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/new/{playerId}")
    @Operation(summary = "Create a new game", description = "Create a new Blackjack game")
    @ApiResponse(responseCode = "201", description = "Game created successfully")
    @ApiResponse(responseCode = "400", description = "This player id is not valid")
    public Mono<ResponseEntity<Map<String, Object>>> createGame(@PathVariable String playerId) {
        if (playerId == null || playerId.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "Player id can't be null or be left empty")));
        }
        return gameService.createGame(playerId)
                .map(game -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("gameId", game.getId());
                    response.put("player", game.getPlayer());
                    response.put("playerCards", game.getPlayerCardsAsString());
                    response.put("dealerCards", game.getDealerCardsAsString());
                    response.put("playerCardsValue", game.getPlayerCardsValue());
                    response.put("dealerCardsValue", game.getDealer().getCardsValue());
                    response.put("isActive", game.isActive());
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get game details", description = "Give the details of a specific game by its id.")
    @ApiResponse(responseCode = "200", description = "Game found")
    @ApiResponse(responseCode = "404", description = "Game not found")
    public Mono<ResponseEntity<Map<String, Object>>> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id)
                .map(game -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("gameId", game.getId());
                    response.put("player", game.getPlayer());
                    response.put("playerCards", game.getPlayerCardsAsString());
                    response.put("dealerCards", game.getDealerCardsAsString());
                    response.put("playerCardsValue", game.getPlayerCardsValue());
                    response.put("dealerCardsValue", game.getDealer().getCardsValue());
                    response.put("result", game.getResult());
                    response.put("isActive", game.isActive());
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/play")
    @Operation(summary = "Play a game move", description = "Play a move in an existing Blackjack game")
    @ApiResponse(responseCode = "200", description = "Move played successfully")
    @ApiResponse(responseCode = "404", description = "Game or player could not be found")
    @ApiResponse(responseCode = "400", description = "Invalid move type or id")
    public Mono<ResponseEntity<Map<String, Object>>> playGame(@PathVariable String id, @RequestParam String moveType) {
        return gameService.playGame(id, moveType)
                .map(game -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("gameId", game.getId());
                    response.put("player", game.getPlayer());
                    response.put("playerCards", game.getPlayerCardsAsString());
                    response.put("dealerCards", game.getDealerCardsAsString());
                    response.put("playerCardsValue", game.getPlayerCardsValue());
                    response.put("dealerCardsValue", game.getDealer().getCardsValue());
                    response.put("result", game.getResult());
                    response.put("isActive", game.isActive());
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.badRequest().body(Map.of("error", e.getMessage()))));
    }

    @GetMapping
    @Operation(summary = "Get all player rankings", description = "Show the rankings of players ordered by number of wins")
    @ApiResponse(responseCode = "200", description = "Rankings shown successfully")
    public Mono<ResponseEntity<Flux<Ranking>>> getAllRanking() {
        return Mono.just(ResponseEntity.ok(gameService.getAllPlayerRankings()));
    }
}

