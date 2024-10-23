package cat.itacademy.s05.t01.n01.blackjack.repository;

import cat.itacademy.s05.t01.n01.blackjack.model.Score;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ScoreRepository extends ReactiveMongoRepository<Score, String> {
    Mono<Score> findByUserId(String userId);
}
