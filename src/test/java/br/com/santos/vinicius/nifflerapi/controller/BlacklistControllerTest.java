package br.com.santos.vinicius.nifflerapi.controller;

import br.com.santos.vinicius.nifflerapi.exception.ElementAlreadyReportedException;
import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModelData;
import br.com.santos.vinicius.nifflerapi.model.dto.BlacklistDto;
import br.com.santos.vinicius.nifflerapi.model.entity.BlacklistEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.model.response.SuccessResponse;
import br.com.santos.vinicius.nifflerapi.repository.BlacklistRepository;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;
import br.com.santos.vinicius.nifflerapi.service.BlacklistService;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BlacklistControllerTest {


    @MockBean
    BlacklistService blacklistService;

    @MockBean
    BlacklistRepository blacklistRepository;

    @MockBean
    UserService userService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "zvinniie", password = "test password")
    public void it_should_add_user_in_blacklist() throws Exception {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        SuccessResponse successResponse = new SuccessResponse(List.of(blacklistEntityExpected), "User added in blacklist.");
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.CREATED).body(new Response(successResponse));

        String randomUserUUID = UUID.randomUUID().toString();

        UserEntity user = new UserEntity(55547L, "username_test", "username_test", 0.0, 0.0);
        user.setId(randomUserUUID);
        user.setCreatedAt(createdAt);

        BlacklistDto blacklistDtoRequest = new BlacklistDto("username_test");

        TwitchUserModelData data = new TwitchUserModelData();
        data.setId("55547");
        data.setLogin("username_test");
        data.setDisplay_name("username_test");
        TwitchUserModel twitchUser = new TwitchUserModel();
        twitchUser.setData(List.of(data));

        when(blacklistRepository.save(any(BlacklistEntity.class))).thenReturn(blacklistEntityExpected);
        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userService.fetchUserByUsername(any(String.class))).thenReturn(user);
        when(userService.getTwitchUser(any(String.class))).thenReturn(twitchUser);
        when(blacklistService.addUserInBlacklist(any(BlacklistDto.class))).thenReturn(requestResponse);

        requestResponse = blacklistService.addUserInBlacklist(blacklistDtoRequest);

        assertNotNull(requestResponse);

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(blacklistDtoRequest);

        mockMvc.perform(post("/v2/blacklist/")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.message").value("User added in blacklist."))
                .andExpect(jsonPath("$.data.recordCount").value(1))
                .andExpect(jsonPath("$.data.records.[0].id").value(randomUUID))
                .andExpect(jsonPath("$.data.records.[0].username").value("username_test"))
                .andExpect(jsonPath("$.data.records.[0].userId").value("55547"));


    }

    @Test
    @WithMockUser(username = "zvinniie", password = "test password")
    public void it_should_get_user_from_blacklist_by_username() throws Exception {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        String input = "username_test";

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        SuccessResponse successResponse = new SuccessResponse(List.of(blacklistEntityExpected), "User found in blacklist.");
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));

        when(blacklistService.getUserInBlacklistByUsername(any(String.class))).thenReturn(requestResponse);

        requestResponse = blacklistService.getUserInBlacklistByUsername(input);

        assertNotNull(requestResponse);

        mockMvc.perform(get("/v2/blacklist/username/{username}", input)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("User found in blacklist."))
                .andExpect(jsonPath("$.data.recordCount").value(1))
                .andExpect(jsonPath("$.data.records.[0].id").value(randomUUID))
                .andExpect(jsonPath("$.data.records.[0].username").value("username_test"))
                .andExpect(jsonPath("$.data.records.[0].userId").value("55547"));


    }

    @Test
    @WithMockUser(username = "zvinniie", password = "test password")
    public void it_should_get_user_from_blacklist_by_userid() throws Exception {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        Long input = 55547L;

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        SuccessResponse successResponse = new SuccessResponse(List.of(blacklistEntityExpected), "User found in blacklist.");
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));

        when(blacklistService.getUserInBlacklistByUserId(any(Long.class))).thenReturn(requestResponse);

        requestResponse = blacklistService.getUserInBlacklistByUserId(input);

        assertNotNull(requestResponse);

        mockMvc.perform(get("/v2/blacklist/id/{userId}", input.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("User found in blacklist."))
                .andExpect(jsonPath("$.data.recordCount").value(1))
                .andExpect(jsonPath("$.data.records.[0].id").value(randomUUID))
                .andExpect(jsonPath("$.data.records.[0].username").value("username_test"))
                .andExpect(jsonPath("$.data.records.[0].userId").value("55547"));


    }

    @Test
    @WithMockUser(username = "zvinniie", password = "test password")
    public void it_should_delete_user_from_blacklist_by_username() throws Exception {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        String input = "username_test";

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        when(blacklistService.removeUserFromBlacklistByUsername(any(String.class))).thenReturn(requestResponse);

        requestResponse = blacklistService.removeUserFromBlacklistByUsername(input);

        assertNotNull(requestResponse);

        mockMvc.perform(delete("/v2/blacklist/username/{userId}", input)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());


    }

    @Test
    @WithMockUser(username = "zvinniie", password = "test password")
    public void it_should_delete_user_from_blacklist_by_userid() throws Exception {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        Long input = 55547L;

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        when(blacklistService.removeUserFromBlacklistByUserId(any(Long.class))).thenReturn(requestResponse);

        requestResponse = blacklistService.removeUserFromBlacklistByUserId(input);

        assertNotNull(requestResponse);

        mockMvc.perform(delete("/v2/blacklist/id/{userId}", input.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());


    }

    @Test
    @WithMockUser(username = "zvinniie", password = "test password")
    public void it_should_get_all_users_from_blacklist() throws Exception {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();
        List<BlacklistEntity> blacklistEntities = new ArrayList<>();

        String randomUUID = UUID.randomUUID().toString();
        String randomUUIDOther = UUID.randomUUID().toString();
        String randomUUIDOtherOther = UUID.randomUUID().toString();
        Date createdAt = new Date();

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test_1");
        blacklistEntityExpected.setCreatedAt(createdAt);
        blacklistEntities.add(blacklistEntityExpected);

        blacklistEntityExpected = new BlacklistEntity();
        blacklistEntityExpected.setUserId(555487L);
        blacklistEntityExpected.setId(randomUUIDOther);
        blacklistEntityExpected.setUsername("username_test_2");
        blacklistEntityExpected.setCreatedAt(createdAt);
        blacklistEntities.add(blacklistEntityExpected);

        blacklistEntityExpected = new BlacklistEntity();
        blacklistEntityExpected.setUserId(5554787L);
        blacklistEntityExpected.setId(randomUUIDOtherOther);
        blacklistEntityExpected.setUsername("username_test_3");
        blacklistEntityExpected.setCreatedAt(createdAt);
        blacklistEntities.add(blacklistEntityExpected);

        List<Object> object = Stream.of(blacklistEntities).flatMap(Collection::stream).collect(Collectors.toList());
        SuccessResponse successResponse = new SuccessResponse(object, "Users were found in blacklist.");
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));

        when(blacklistService.getAllUsersInBlacklist()).thenReturn(requestResponse);

        requestResponse = blacklistService.getAllUsersInBlacklist();

        assertNotNull(requestResponse);

        mockMvc.perform(get("/v2/blacklist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("Users were found in blacklist."))
                .andExpect(jsonPath("$.data.recordCount").value(3))
                .andExpect(jsonPath("$.data.records.[0].id").value(randomUUID))
                .andExpect(jsonPath("$.data.records.[0].username").value("username_test_1"))
                .andExpect(jsonPath("$.data.records.[0].userId").value("55547"))
                .andExpect(jsonPath("$.data.records.[1].id").value(randomUUIDOther))
                .andExpect(jsonPath("$.data.records.[1].username").value("username_test_2"))
                .andExpect(jsonPath("$.data.records.[1].userId").value("555487"))
                .andExpect(jsonPath("$.data.records.[2].id").value(randomUUIDOtherOther))
                .andExpect(jsonPath("$.data.records.[2].username").value("username_test_3"))
                .andExpect(jsonPath("$.data.records.[2].userId").value("5554787"));


    }

    @Test
    @WithMockUser(username = "zvinniie", password = "test password")
    public void it_should_throw_not_found_exception() throws Exception {

        when(blacklistService.getAllUsersInBlacklist()).thenThrow(new NoSuchElementFoundException("User batata does not exists."));


        mockMvc.perform(get("/v2/blacklist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "zvinniie", password = "test password")
    public void it_should_throw_already_reported_exception() throws Exception {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        String randomUserUUID = UUID.randomUUID().toString();

        UserEntity user = new UserEntity(55547L, "username_test", "username_test", 0.0, 0.0);
        user.setId(randomUserUUID);
        user.setCreatedAt(createdAt);

        BlacklistDto blacklistDtoRequest = new BlacklistDto("username_test");

        TwitchUserModelData data = new TwitchUserModelData();
        data.setId("55547");
        data.setLogin("username_test");
        data.setDisplay_name("username_test");
        TwitchUserModel twitchUser = new TwitchUserModel();
        twitchUser.setData(List.of(data));

        when(blacklistService.addUserInBlacklist(any(BlacklistDto.class))).thenThrow(new ElementAlreadyReportedException("User batata already in blacklist."));

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(blacklistDtoRequest);

        assertThrows(ElementAlreadyReportedException.class, () -> blacklistService.addUserInBlacklist(blacklistDtoRequest));

        mockMvc.perform(post("/v2/blacklist")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAlreadyReported());

    }

}
