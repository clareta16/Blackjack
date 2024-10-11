package repository;

import model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameInterface {
    public interface GameRepository extends ReactiveMongoRepository<Game, String> {
    }
}
