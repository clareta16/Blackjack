package cat.itacademy.s05.t01.n01.blackjack.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@Configuration
public class MySqlconfig {

    private final DatabaseClient databaseClient;

    public MySqlconfig(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @PostConstruct
    public void initializeDatabase() {
        databaseClient.sql("CREATE TABLE IF NOT EXISTS players ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "username VARCHAR(255) NOT NULL, "
                        + "is_playing BOOLEAN DEFAULT FALSE, "
                        + "wins INT DEFAULT 0, "
                        + "total_cards_value INT DEFAULT 0);")
                .then()
                .doOnSuccess(unused -> {
                    System.out.println("Table created if it did not exist.");
                })
                .doOnError(error -> System.err.println("Error creating table: " + error.getMessage()))
                .subscribe();
    }
}
