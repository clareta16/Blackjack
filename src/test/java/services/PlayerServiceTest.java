package services;

import model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import repository.PlayerRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    // Simula el repositori sense connectar-se a la db

    @InjectMocks
    private PlayerService playerService;
    // Injecta el mock del repositori dins del servei

    @Test
    public void testGetPlayerById() {
        Player player = new Player("1", "Clareta");

        when(playerRepository.findById("1")).thenReturn(Mono.just(player));
        // Simula la crida al repositori anterior


        Mono<Player> result = playerService.getPlayerDetails("1");
        assertEquals(player, result.block());
        // Comprova que torna el jugador que toca
        // compara el jugador simulat amb el jugador obtingut
    }

    @Test
    public void testChangeUsername() {
        Player existingPlayer = new Player("2", "Daniela");
        existingPlayer.setUsername("Danny");

        Mono<Player> result = playerService.changePlayerUsername("2", "Danny");
        assertEquals(existingPlayer, result.block());
    }
}

