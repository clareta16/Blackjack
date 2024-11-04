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

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player existingPlayer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        existingPlayer = new Player("TestPlayer");
        existingPlayer.setId("1234");
    }

    @Test
    void testChangePlayerUsername_Success() {
        String playerId = existingPlayer.getId();
        String newUsername = "Juanca";

        when(playerRepository.findById(playerId)).thenReturn(Mono.just(existingPlayer));

        existingPlayer.setUsername(newUsername);

        when(playerRepository.save(existingPlayer)).thenReturn(Mono.just(existingPlayer));

        Mono<Player> result = playerService.changePlayerUsername(playerId, newUsername);

        StepVerifier.create(result)
                .expectNextMatches(updatedPlayer -> {
                    assertEquals(newUsername, updatedPlayer.getUsername());
                    return true;
                })
                .verifyComplete();

        verify(playerRepository, times(1)).save(existingPlayer);
    }

    @Test
    public void testChangePlayerUsername_PlayerNotFound() {
        String playerId = existingPlayer.getId();
        when(playerRepository.findById(playerId)).thenReturn(Mono.empty());

        Mono<Player> result = playerService.changePlayerUsername(playerId, "mariaAntonieta");

        StepVerifier.create(result)
                .expectError(PlayerNotFoundException.class)
                .verify();

        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testDeletePlayer_Success() {
        String playerId = existingPlayer.getId();
        when(playerRepository.findById(playerId)).thenReturn(Mono.just(existingPlayer));

        Mono<Void> result = playerService.deletePlayer(playerId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(playerRepository, times(1)).delete(existingPlayer);
    }

    @Test
    public void testDeletePlayer_PlayerNotFound() {
        String playerId = existingPlayer.getId();
        when(playerRepository.findById(playerId)).thenReturn(Mono.empty());

        Mono<Void> result = playerService.deletePlayer(playerId);

        StepVerifier.create(result)
                .expectError(PlayerNotFoundException.class)
                .verify();

        verify(playerRepository, never()).delete(any(Player.class));
    }
}

