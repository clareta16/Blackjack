package cat.itacademy.s05.t01.n01.blackjack.services;

import cat.itacademy.s05.t01.n01.blackjack.model.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.blackjack.repository.ScoreRepository;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    public Mono<Score> saveScore(Score score) {
        return scoreRepository.save(score);
    }

    public Mono<Score> getScoresByUserId(String id) {
        return scoreRepository.findByUserId(id);
    }

    public Flux<Score> getAllScores() {
        return scoreRepository.findAll();
    }
}
