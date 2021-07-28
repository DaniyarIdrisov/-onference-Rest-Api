package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.RoomDto;
import waveaccess.daniyar.idrisov.conferencerestapi.services.RoomsService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("RoomsController is testing")
public class RoomsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomsService roomsService;

    @BeforeEach
    public void setRoomsService() {

        when(roomsService.getRoomById(1L)).thenReturn(
                RoomDto.builder()
                        .id(1L)
                        .roomNumber("1310")
                        .build()
        );

        when(roomsService.getRoomById(-10L)).thenThrow(IllegalArgumentException.class);

    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getRoomById() is testing")
    class GetRoomById {

        @Test
        public void get_room_by_id() throws Exception {
            mockMvc.perform(get("/rooms/{room-id}", "1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id", is(1)))
                    .andExpect(jsonPath("roomNumber", is("1310")));

        }

        @Test
        public void get_room_by_incorrect_id() throws Exception {
            mockMvc.perform(get("/rooms/{room-id}", "-10"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }


}
