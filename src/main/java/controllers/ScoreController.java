package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import services.ScoreService;

@RestController
@RequestMapping("/score")
@Tag(name = "Score Management System", description = "Operations pertaining to player scores in Blackjack games")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping("/new")
    @Operation(summary = "Create or update a score", description = "Create or update a player's score.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Score created or updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Score.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error while saving score")
    })
    public Mono<ResponseEntity<Score>> createScore(@RequestBody Score score) {
        return scoreService.saveScore(score)
                .map(savedScore -> ResponseEntity.status(HttpStatus.CREATED).body(savedScore))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping
    @Operation(summary = "Get all scores", description = "Retrieve all player scores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scores retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Score.class)))
    })
    public Flux<Score> getAllScores() {
        return scoreService.getAllScores();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get scores by user ID", description = "Retrieve scores of a specific user by their user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scores retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Score.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<Score> getScoresByUserId(@PathVariable String userId) {
        return scoreService.getScoresByUserId(userId);
    }
}




