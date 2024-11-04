package controllers;

import cat.itacademy.s05.t01.n01.blackjack.controllers.GameController;
import cat.itacademy.s05.t01.n01.blackjack.exception.GameNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;
    @Mock
    private PlayerRepository playerRepository;


    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateGame_PlayerNotFound() {
        // Arrange
        String playerId = "invalidId";

        when(playerRepository.findById(playerId)).thenReturn(Mono.empty());

        // Act
        Mono<ResponseEntity<Game>> response = gameController.createGame(playerId);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                    return true;
                })
                .verifyComplete();

        verify(playerRepository, times(1)).findById(playerId);
    }

    @Test
    void testCreateGame_Success() {
        // Arrange
        String playerId = "existingId";
        Player existingPlayer = new Player("TestPlayer");
        existingPlayer.setId(playerId);
        Game createdGame = new Game(existingPlayer);

        when(playerRepository.findById(playerId)).thenReturn(Mono.just(existingPlayer));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(createdGame));

        // Act
        Mono<ResponseEntity<Game>> response = gameController.createGame(playerId);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertEquals(createdGame, responseEntity.getBody());
                    return true;
                })
                .verifyComplete();

        verify(playerRepository, times(1)).findById(playerId);
        verify(gameRepository, times(1)).save(any(Game.class));
    }


    @Test
    void testGetGameDetails_Success() {
        String gameId = "1";
        String playerName = "TestPlayer";
        Game game = new Game(new Player(playerName));
        game.setId(gameId);

        when(gameService.getGameDetails(gameId)).thenReturn(Mono.just(game));

        Mono<ResponseEntity<Game>> response = gameController.getGameDetails(gameId);

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals(gameId, responseEntity.getBody().getId());
                    return true;
                })
                .verifyComplete();

        verify(gameService, times(1)).getGameDetails(gameId);
    }

    @Test
    void testGetGameDetails_NotFound() {
        String gameId = "1";

        when(gameService.getGameDetails(gameId)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Game>> response = gameController.getGameDetails(gameId);

        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode()))
                .verifyComplete();

        verify(gameService, times(1)).getGameDetails(gameId);
    }

    @Test
    void testPlayGame_Success() {
        // Arrange
        String gameId = "1";
        String moveType = "hit";

        String playerName = "TestPlayer";
        Game game = new Game(new Player(playerName));
        game.setId(gameId);

        // Mock the service to return the game when playGame is called
        when(gameService.playGame(gameId, moveType)).thenReturn(Mono.just(game));

        // Act
        Mono<ResponseEntity<Game>> response = gameController.playGame(gameId, moveType);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); // Check status code
                    assertEquals(gameId, responseEntity.getBody().getId()); // Check game ID
                    return true;
                })
                .verifyComplete();

        // Verify that the service was called once
        verify(gameService, times(1)).playGame(gameId, moveType);
    }

    @Test
    void testGetAllRanking_Success() {
        // Arrange
        Ranking ranking1 = new Ranking("Player1", 5);
        Ranking ranking2 = new Ranking("Player2", 3);
        Flux<Ranking> rankings = Flux.just(ranking1, ranking2);  // Create a Flux of rankings

        // Mock the service to return the list of rankings
        when(gameService.getAllPlayerRankings()).thenReturn(rankings);

        // Act
        Mono<ResponseEntity<Flux<Ranking>>> response = gameController.getAllRanking();

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());  // Check status code
                    assertNotNull(responseEntity.getBody());  // Ensure body is not null

                    // Verify that the response body is a Flux and contains the expected rankings
                    StepVerifier.create(responseEntity.getBody())
                            .expectNext(ranking1)  // Check first ranking
                            .expectNext(ranking2)  // Check second ranking
                            .verifyComplete();

                    return true; // Ensure the test completes successfully
                })
                .verifyComplete();  // Complete the verification

        // Verify that the service was called once
        verify(gameService, times(1)).getAllPlayerRankings();
    }

}









