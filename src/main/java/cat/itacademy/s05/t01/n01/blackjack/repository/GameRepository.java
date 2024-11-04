package cat.itacademy.s05.t01.n01.blackjack.repository;

import cat.itacademy.s05.t01.n01.blackjack.model.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String> {
    public Mono<Game> findById(String id);
}
