package services;

import model.Player;
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
import repository.PlayerRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePlayer() {
        String username = "Clara";
        Player player = new Player(username);

        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(player));
        Mono<Player> result = playerService.createPlayer(username);

        // verifica la funcionalitat dels mono o flux publishers creant un escenari de test
        StepVerifier.create(result)
                .expectNextMatches(createdPlayer ->
                        createdPlayer.getUsername().equals(username))
                .verifyComplete(); // comprovar que no hi ha errors

        verify(playerRepository, times(1)).save(any(Player.class));
        //verify: comprova si un mètode específic sha cridat
        // times: vegades que s'espera que es cridi el mètode
    }

    @Test
    public void testGetPlayerById() {
        Player player = new Player("Clareta");

        when(playerRepository.findById("1")).thenReturn(Mono.just(player));

        Mono<Player> result = playerService.getPlayerDetails("1");
        assertEquals(player, result.block());
    }
}


