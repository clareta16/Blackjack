package controllers;

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
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping("/new")
    public Mono<ResponseEntity<Score>> createScore(@RequestBody Score score) {
        return scoreService.saveScore(score)
                .map(savedScore -> ResponseEntity.status(HttpStatus.CREATED).body(savedScore))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping
    public Flux<Score> getAllScores() {
        return scoreService.getAllScores();
    }

    @GetMapping("/user/{userId}")
    public Mono<Score> getScoresByUserId(@PathVariable String userId) {
        return scoreService.getScoresByUserId(userId);
    }
}




