package services;

import cat.itacademy.s05.t01.n01.blackjack.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.blackjack.model.Player;
import cat.itacademy.s05.t01.n01.blackjack.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.any;
import cat.itacademy.s05.t01.n01.blackjack.repository.PlayerRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.just;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.just;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to initialize mocks
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player existingPlayer;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        existingPlayer = new Player("TestPlayer");
        existingPlayer.setId("1234"); // Ensure ID matches
    }

    @Test
    void testChangePlayerUsername_Success() {
        // Arrange
        String playerId = existingPlayer.getId();
        String newUsername = "NewUsername";

        // Mock the repository to return the existing player when searched by ID
        when(playerRepository.findById(playerId)).thenReturn(Mono.just(existingPlayer));

        // Update the player's username to the new username
        existingPlayer.setUsername(newUsername);

        // Mock the save method to return the updated player
        when(playerRepository.save(existingPlayer)).thenReturn(Mono.just(existingPlayer));

        // Act
        Mono<Player> result = playerService.changePlayerUsername(playerId, newUsername);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(updatedPlayer -> {
                    assertEquals(newUsername, updatedPlayer.getUsername());
                    return true;
                })
                .verifyComplete();

        // Verify that the repository save was called once with the updated player
        verify(playerRepository, times(1)).save(existingPlayer);
    }

    @Test
    public void testChangePlayerUsername_PlayerNotFound() {
        // Arrange: Simulate that the repository does not find the player
        String playerId = existingPlayer.getId();
        when(playerRepository.findById(playerId)).thenReturn(Mono.empty());

        // Act: Call the method and verify the error
        Mono<Player> result = playerService.changePlayerUsername(playerId, "SomeUsername");

        // Assert: Expect an error to be thrown
        StepVerifier.create(result)
                .expectError(PlayerNotFoundException.class)
                .verify();

        // Verify that the save method was not called since the player was not found
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testDeletePlayer_Success() {
        // Arrange: Mock the repository to return the existing player when searched by ID
        String playerId = existingPlayer.getId();
        when(playerRepository.findById(playerId)).thenReturn(Mono.just(existingPlayer));

        // Act: Call the deletePlayer method
        Mono<Void> result = playerService.deletePlayer(playerId);

        // Assert: Verify that the deletion completes successfully
        StepVerifier.create(result)
                .verifyComplete();

        // Verify that the delete method was called once with the existing player
        verify(playerRepository, times(1)).delete(existingPlayer);
    }

    @Test
    public void testDeletePlayer_PlayerNotFound() {
        // Arrange: Simulate that the repository does not find the player
        String playerId = existingPlayer.getId();
        when(playerRepository.findById(playerId)).thenReturn(Mono.empty());

        // Act: Call the deletePlayer method and expect an error
        Mono<Void> result = playerService.deletePlayer(playerId);

        // Assert: Expect an error to be thrown
        StepVerifier.create(result)
                .expectError(PlayerNotFoundException.class)
                .verify();

        // Verify that the delete method was never called
        verify(playerRepository, never()).delete(any(Player.class));
    }
}





