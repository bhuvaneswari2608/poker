package integration;

import DTO.GameDTO;
import DTO.PlayerDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.Main;
import controller.GameController;
import entity.Game;
import entity.Player;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import repository.GameEventRepository;
import repository.GameRepository;

import service.impl.CardServiceImpl;
import service.impl.GameEventManager;
import service.impl.GameServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


    @SpringBootTest
    @ContextConfiguration(classes={Main.class})
  //  @TestPropertySource(locations = "classpath:application-test.properties")
    public class GameIntegrationTest {

        private MockMvc mockMvc;

        @InjectMocks
        private GameController gameController;

        @Mock
        private GameRepository repository;

        @Mock
        private GameEventRepository gameEventRepository;

        @Mock
        private GameServiceImpl gameService;

        @Mock
        private CardServiceImpl cardService;

        @MockBean
        private GameEventManager listener;

        private static final UUID PLAYER1_ID = UUID.randomUUID();

        private static final UUID GAME_ID = UUID.randomUUID();

        private static final PlayerDTO PLAYER_DTO = PlayerDTO.builder().id(PLAYER1_ID).build();

        private static final Player PLAYER = Player.builder().id(PLAYER1_ID).build();

        private static final Game GAME = Game.builder().players(List.of(PLAYER)).deck(1).cardPerPlayer(2).id(GAME_ID).build();

        private static final GameDTO GAME_DTO = GameDTO.builder().players(List.of(PLAYER_DTO)).deck(1).numberOfCardsPerPlayer(2).uuid(GAME_ID).build();


        private final ObjectMapper objectMapper = new ObjectMapper();


        @BeforeEach
        public void setUp() {
            mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
        }


        //tofix: the beans are null
        @Disabled
        @Test
        public void createGame_withPlayerAndDeck_expectGameCreated() throws Exception {

            GameDTO expected = GameDTO.builder().players(List.of(PLAYER_DTO)).deck(1).numberOfCardsPerPlayer(2).uuid(GAME_ID).unDealtCards(new ArrayList<>()).build();


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

            repository.findById(responseDto.getUuid());
            repository.delete(responseDto.getUuid());

        }


    }

