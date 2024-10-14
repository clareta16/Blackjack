package controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import services.PlayerService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/player")
@Tag(name = "Player Management System", description = "Operations related to managing Blackjack players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/new")
    @Operation(summary = "Create a new player", description = "Create a new player with the specified username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or player creation failed")
    })
    public Mono<ResponseEntity<Player>> createPlayer(@RequestParam String username) {
        return playerService.createPlayer(username)
                .map(player -> ResponseEntity.status(201).body(player))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player details", description = "Retrieve details of a specific player by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public Mono<ResponseEntity<Player>> getPlayerDetails(@PathVariable String id) {
        return playerService.getPlayerDetails(id)
                .map(player -> ResponseEntity.ok(player));
    }

    @PutMapping("/player/{playerId}")
    @Operation(summary = "Change player's username", description = "Change the username of a specific player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public Mono<ResponseEntity<Player>> changePlayerUsername(@PathVariable String playerId, @RequestBody String newUsername) {
        return playerService.changePlayerUsername(playerId, newUsername)
                .map(player -> ResponseEntity.ok(player))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    @Operation(summary = "Get all players", description = "Retrieve the list of all players.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Players retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
    })
    public Flux<ResponseEntity<Player>> getAllPlayers() {
        return playerService.getAllPlayers()
                .map(player -> ResponseEntity.ok(player));
    }

    @PostMapping("/{playerId}/bet")
    @Operation(summary = "Place a bet", description = "Place a bet for a specific player in a Blackjack game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bet placed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or bet placement failed")
    })
    public Mono<ResponseEntity<Player>> placeBet(
            @PathVariable String playerId,
            @RequestParam String gameId,
            @RequestParam int amount) {

        return playerService.placeBet(gameId, playerId, amount)
                .map(player -> ResponseEntity.ok(player))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}

