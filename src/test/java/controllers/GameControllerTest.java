package controllers;

import cat.itacademy.s05.t01.n01.blackjack.controllers.GameController;
import cat.itacademy.s05.t01.n01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.model.Game;
import cat.itacademy.s05.t01.n01.blackjack.model.Player;
import cat.itacademy.s05.t01.n01.blackjack.model.Ranking;
import cat.itacademy.s05.t01.n01.blackjack.repository.GameRepository;
import cat.itacademy.s05.t01.n01.blackjack.repository.PlayerRepository;
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
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateGame_PlayerNotFound() {
        String playerId = "notExistingId";

        when(gameService.createGame(playerId)).thenReturn(Mono.error(new PlayerNotFoundException("Player not found")));

        Mono<ResponseEntity<Map<String, Object>>> response = gameController.createGame(playerId);

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                    return true;
                })
                .verifyComplete();

        verify(gameService, times(1)).createGame(playerId);
    }

    @Test
    void testCreateGame_Success() {
        String playerId = "existingId";
        Player existingPlayer = new Player("TestPlayer");
        Game createdGame = new Game(existingPlayer);

        when(gameService.createGame(playerId)).thenReturn(Mono.just(createdGame));
        Mono<ResponseEntity<Map<String, Object>>> response = gameController.createGame(playerId);

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertEquals(createdGame.getId(), responseEntity.getBody().get("gameId"));
                    return true;
                })
                .verifyComplete();

        verify(gameService, times(1)).createGame(playerId);
    }

    @Test
    void testGetGameDetails_Success() {
        String gameId = "1";
        Game game = new Game(new Player("TestPlayer"));
        game.setId(gameId);

        when(gameService.getGameDetails(gameId)).thenReturn(Mono.just(game));
        Mono<ResponseEntity<Map<String, Object>>> response = gameController.getGameDetails(gameId);

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals(gameId, responseEntity.getBody().get("gameId"));
                    return true;
                })
                .verifyComplete();

        verify(gameService, times(1)).getGameDetails(gameId);
    }

    @Test
    void testGetGameDetails_NotFound() {
        String gameId = "1";

        when(gameService.getGameDetails(gameId)).thenReturn(Mono.empty());
        Mono<ResponseEntity<Map<String, Object>>> response = gameController.getGameDetails(gameId);

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode()))
                .verifyComplete();

        verify(gameService, times(1)).getGameDetails(gameId);
    }

    @Test
    void testPlayGame_Success() {
        String gameId = "1";
        String moveType = "hit";
        Game game = new Game(new Player("TestPlayer"));
        game.setId(gameId);
        game.setResult("Player wins!");

        when(gameService.playGame(gameId, moveType)).thenReturn(Mono.just(game));

        Mono<ResponseEntity<Map<String, Object>>> response = gameController.playGame(gameId, moveType);

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals(gameId, responseEntity.getBody().get("gameId"));
                    return true;
                })
                .verifyComplete();

        verify(gameService, times(1)).playGame(gameId, moveType);
    }

    @Test
    void testPlayGame_NotFound() {
        String gameId = "invalidId";
        String moveType = "hit";

        when(gameService.playGame(gameId, moveType)).thenReturn(Mono.error(new GameNotFoundException("Game not found")));


        Mono<ResponseEntity<Map<String, Object>>> response = gameController.playGame(gameId, moveType);

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode()))
                .verifyComplete();

        verify(gameService, times(1)).playGame(gameId, moveType);
    }

    @Test
    void testGetAllRanking_Success() {
        Ranking ranking1 = new Ranking("Player1", 5);
        Ranking ranking2 = new Ranking("Player2", 3);
        Flux<Ranking> rankings = Flux.just(ranking1, ranking2);

        when(gameService.getAllPlayerRankings()).thenReturn(rankings);

        Mono<ResponseEntity<Flux<Ranking>>> response = gameController.getAllRanking();

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());

                    StepVerifier.create(responseEntity.getBody())
                            .expectNext(ranking1)
                            .expectNext(ranking2)
                            .verifyComplete();

                    return true;
                })
                .verifyComplete();

        verify(gameService, times(1)).getAllPlayerRankings();
    }
}


