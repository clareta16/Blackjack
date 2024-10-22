package controllers;

import cat.itacademy.s05.t01.n01.blackjack.controllers.GameController;
import cat.itacademy.s05.t01.n01.blackjack.model.Game;
import cat.itacademy.s05.t01.n01.blackjack.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import cat.itacademy.s05.t01.n01.blackjack.services.GameService;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

class GameControllerTest {
    private GameService gameService;
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        gameService = Mockito.mock(GameService.class);
        gameController = new GameController(gameService);
    }

    @Test
    public void testGetRanking() {
        List<String> rankings = Arrays.asList("Clara", "Raquel", "Arnau");

        when(gameService.getRanking()).thenReturn(Flux.fromIterable(rankings));

        Flux<ResponseEntity<String>> result = gameController.getRanking();

        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok("Clara"))
                .expectNext(ResponseEntity.ok("Raquel"))
                .expectNext(ResponseEntity.ok("Arnau"))
                .verifyComplete();

        //expectNext especifica el valor esperat que hauria d'emtre el flux

        verify(gameService, times(1)).getRanking();
    }

    @Test
    public void testCreateGameOK() {
        Player player1 = new Player("Clara");
        Player player2 = new Player("Raquel");

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        Game game1 = new Game();
        game1.setId("134");
        game1.setPlayers(players);


        when(gameService.createGame(players)).thenReturn(Mono.just(game1));
        Mono<ResponseEntity<Game>> result = gameController.createGame(players);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == CREATED && response.getBody().getId().equals("134"))
                .verifyComplete();

        verify(gameService, times(1)).createGame(players);
    }

    @Test
    public void testCreateGameNotOK() {
        Player player1 = new Player("");
        Player player2 = new Player("");

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        when(gameService.createGame(players)).thenReturn(Mono.empty());
        Mono<ResponseEntity<Game>> result = gameController.createGame(players);

        StepVerifier.create(result)
                .expectNext(ResponseEntity.badRequest().build())
                .verifyComplete();

        verify(gameService, times(1)).createGame(players);
    }
}


