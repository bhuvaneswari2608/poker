package controller;

import DTO.GameDTO;
import DTO.PlayerDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Game;
import entity.Player;
import enums.GameEvent;
import exception.GameUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class GameControllerTest {


    private static final UUID PLAYER1_ID = UUID.randomUUID();

    private static final UUID GAME_ID = UUID.randomUUID();

    private static final PlayerDTO PLAYER_DTO = PlayerDTO.builder().id(PLAYER1_ID).build();

    private static final Player PLAYER = Player.builder().id(PLAYER1_ID).build();

    private static final Game GAME = Game.builder().players(List.of(PLAYER)).deck(1).cardPerPlayer(2).id(GAME_ID).build();

    private static final GameDTO GAME_DTO = GameDTO.builder().players(List.of(PLAYER_DTO)).deck(1).numberOfCardsPerPlayer(2).uuid(GAME_ID).build();

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }


     @Test
    public void createGame_withPlayersAndDeck_expectGameCreated() throws Exception {

        GameDTO expected = GameDTO.builder().players(List.of(PLAYER_DTO)).deck(1).numberOfCardsPerPlayer(2).uuid(GAME_ID).unDealtCards(new ArrayList<>()).build();

        when(gameService.createGame(any(GameDTO.class))).thenReturn(GAME);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/game/create")
                        .content(objectMapper.writeValueAsString(GAME_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        TypeReference<GameDTO> mapType = new TypeReference<GameDTO>() {
        };
        GameDTO responseDto = objectMapper.readValue(responseBody, mapType);

        assertEquals(expected.getUuid(), responseDto.getUuid());
        assertEquals(expected.getPlayers(), responseDto.getPlayers());
        assertEquals(expected.getDeck(), responseDto.getDeck());

    }

    @Test
    public void updateGame_removeDeck_throwsException() throws Exception {

        GameDTO request = GameDTO.builder().players(List.of(PLAYER_DTO))
                .deck(0).numberOfCardsPerPlayer(2).uuid(GAME_ID)
                .gameEvent(List.of(GameEvent.DECK_ADDED))
                .unDealtCards(new ArrayList<>()).build();

        when(gameService.updateGame(any(GameDTO.class), any())).thenThrow(new GameUpdateException("Update failed since the Game is started"));

         mockMvc.perform(patch("/api/v1/game/"+GAME_ID+"/update")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed());

    }


    @Test
    public void getGame_beforeCardIsDealt_expectGameDTO() throws Exception {

        GameDTO expected = GameDTO.builder().players(List.of(PLAYER_DTO))
                .deck(1).numberOfCardsPerPlayer(2).uuid(GAME_ID).unDealtCards(new ArrayList<>()).build();

        when(gameService.getGameById(any(UUID.class), any(), any())).thenReturn(GAME);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/game/"+GAME_ID))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        TypeReference<GameDTO> mapType = new TypeReference<GameDTO>() {
        };
        GameDTO responseDto = objectMapper.readValue(responseBody, mapType);

        assertEquals(expected.getUuid(), responseDto.getUuid());
        assertEquals(expected.getDeck(), responseDto.getDeck());

    }

}
