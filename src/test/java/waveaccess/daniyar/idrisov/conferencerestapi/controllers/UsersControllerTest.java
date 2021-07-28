package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;
import waveaccess.daniyar.idrisov.conferencerestapi.services.UsersService;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("UsersController is testing")
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @BeforeEach
    public void setUsersService() {

        when(usersService.getAllUsers()).thenReturn(Collections.singletonList(
                UserDto.builder()
                        .id(2L)
                        .email("my@mail.ru")
                        .firstName("Da")
                        .lastName("Id")
                        .hashPassword("$2a$10$S9wmXBTEn6qyXCak94AxzOTDL7tgNYfwsgVW4uHsidA5eCfOtD1Q2")
                        .organization("KFU")
                        .confirmation(User.Confirmation.CONFIRMED)
                        .confirmCode("f55b4a06-dab9-478f-9999-0d0f9717035c")
                        .role(User.Role.SPEAKER)
                        .state(User.State.ACTIVE)
                        .build())
        );

        when(usersService.getUsersByRole(User.Role.ADMIN)).thenReturn(Collections.singletonList(
                UserDto.builder()
                        .id(1L)
                        .email("admin@admin.com")
                        .firstName("Admin")
                        .lastName("Adminov")
                        .hashPassword("$2a$10$Lv/zZCl1BJfdJyUubrFop.smTVp43.mDa5QTbKw.6rp1efi28/VgS")
                        .organization("ITS")
                        .confirmation(User.Confirmation.CONFIRMED)
                        .confirmCode("152d3842-d996-431b-9190-1fe9a80cd12d")
                        .role(User.Role.ADMIN)
                        .state(User.State.ACTIVE)
                        .build()
        ));

        when(usersService.getUsersByRole(User.Role.SPEAKER)).thenReturn(Collections.singletonList(
                UserDto.builder()
                        .id(2L)
                        .email("my@mail.ru")
                        .firstName("D")
                        .lastName("I")
                        .hashPassword("$2a$10$S9wmXBTEn6qyXCak94AxzOTDL7tgNYfwsgVW4uHsidA5eCfOtD1Q2")
                        .organization("KFU")
                        .confirmation(User.Confirmation.CONFIRMED)
                        .confirmCode("f55b4a06-dab9-478f-9999-0d0f9717035c")
                        .role(User.Role.SPEAKER)
                        .state(User.State.ACTIVE)
                        .build()
        ));

        when(usersService.getUsersByRole(User.Role.LISTENER)).thenReturn(Collections.singletonList(
             UserDto.builder()
                     .id(3L)
                     .email("his@mail.ru")
                     .firstName("Dan")
                     .lastName("Idris")
                     .hashPassword("$2a$10$uxTjYUnzTMLjTtIv6Hy1Su8n4HBDnL/Pp4tSRFkFzDPRe/PbDwh1S")
                     .organization("K")
                     .confirmation(User.Confirmation.NOT_CONFIRMED)
                     .confirmCode("ce96871a-7656-4072-8483-cd3d09598939")
                     .role(User.Role.LISTENER)
                     .state(User.State.ACTIVE)
                     .build()

        ));

    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getAllUsers() is testing")
    class GetAllUsersTest {

        @Test
        public void get_all_users() throws Exception {
            mockMvc.perform(get("/users")
            .header("Authorization", "81e0c571-f0b6-4fe8-b75b-e4ab9f6636ce"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id", is(2)))
                    .andExpect(jsonPath("$[0].email", is("my@mail.ru")))
                    .andExpect(jsonPath("$[0].role", is("SPEAKER")))
                    .andExpect(jsonPath("$[0].firstName", is("Da")))
                    .andExpect(jsonPath("$[0].lastName", is("Id")));
        }

    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getUsersByRole() is testing")
    class GetUsersByRole {

        @Test
        public void get_admins() throws Exception {
            mockMvc.perform(post("/users/role")
                    .header("Authorization", "81e0c571-f0b6-4fe8-b75b-e4ab9f6636ce")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"role\": \"ADMIN\"\n" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].email", is("admin@admin.com")));
        }

        @Test
        public void get_speakers() throws Exception {
            mockMvc.perform(post("/users/role")
                    .header("Authorization", "81e0c571-f0b6-4fe8-b75b-e4ab9f6636ce")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"role\": \"SPEAKER\"\n" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].email", is("my@mail.ru")));
        }

        @Test
        public void get_listeners() throws Exception {
            mockMvc.perform(post("/users/role")
                    .header("Authorization", "81e0c571-f0b6-4fe8-b75b-e4ab9f6636ce")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"role\": \"LISTENER\"\n" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].email", is("his@mail.ru")));
        }

    }





}
