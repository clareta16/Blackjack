package cat.itacademy.s05.t01.n01.blackjack.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
public class MySqlconfig {

    private final DatabaseClient databaseClient;

    public MySqlconfig(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @PostConstruct
    public void initializeDatabase() {
        databaseClient.sql("""
                            CREATE TABLE IF NOT EXISTS players (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                name VARCHAR(255) NOT NULL,
                                total_victories INT DEFAULT 0,
                                total_defeats INT DEFAULT 0,
                                total_games INT DEFAULT 0
                            );
                        """).then()
                .doOnSuccess(unused -> System.out.println("Table created if it did not exist."))
                .subscribe();

    }
}