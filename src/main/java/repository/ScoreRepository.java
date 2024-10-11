package repository;

import model.Score;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ScoreRepository extends ReactiveMongoRepository<Score, String> {
    Mono<Score> findByUserId(String userId);
}
