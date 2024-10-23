package cat.itacademy.s05.t01.n01.blackjack.repository;

import cat.itacademy.s05.t01.n01.blackjack.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String> {

    }

