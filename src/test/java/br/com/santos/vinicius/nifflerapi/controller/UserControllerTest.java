package br.com.santos.vinicius.nifflerapi.controller;

import br.com.santos.vinicius.nifflerapi.exception.InternalServerException;
import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModelData;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.ErrorResponse;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlacklistService blacklistService;

    @MockBean
    BlacklistRepository blacklistRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void it_should_get_all_users() throws Exception {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(55547L, "username_test_1", "Username_Test_1", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(555487L, "username_test_2", "Username_Test_2", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(5554787L, "username_test_3", "Username_Test_3", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(45461487L, "username_test_4", "Username_Test_4", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        List<Object> object = Stream.of(users).flatMap(Collection::stream).collect(Collectors.toList());
        SuccessResponse successResponse = new SuccessResponse(object, "Users were found.");
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));

        when(userService.getAllUsers()).thenReturn(requestResponse);

        requestResponse = userService.getAllUsers();

        assertNotNull(requestResponse);

        mockMvc.perform(get("/v2/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("Users were found."))
                .andExpect(jsonPath("$.data.recordCount").value(4))
                .andExpect(jsonPath("$.data.records.[0].id").value(firstRandomUUID))
                .andExpect(jsonPath("$.data.records.[0].username").value("username_test_1"))
                .andExpect(jsonPath("$.data.records.[0].displayName").value("Username_Test_1"))
                .andExpect(jsonPath("$.data.records.[0].pointsToAdd").value("0.0"))
                .andExpect(jsonPath("$.data.records.[0].pointsAdded").value("0.0"))
                .andExpect(jsonPath("$.data.records.[0].userId").value("55547"))
                .andExpect(jsonPath("$.data.records.[1].id").value(secondRandomUUID))
                .andExpect(jsonPath("$.data.records.[1].username").value("username_test_2"))
                .andExpect(jsonPath("$.data.records.[1].displayName").value("Username_Test_2"))
                .andExpect(jsonPath("$.data.records.[1].pointsToAdd").value("0.0"))
                .andExpect(jsonPath("$.data.records.[1].pointsAdded").value("0.0"))
                .andExpect(jsonPath("$.data.records.[1].userId").value("555487"))
                .andExpect(jsonPath("$.data.records.[2].id").value(thirdRandomUUID))
                .andExpect(jsonPath("$.data.records.[2].username").value("username_test_3"))
                .andExpect(jsonPath("$.data.records.[2].displayName").value("Username_Test_3"))
                .andExpect(jsonPath("$.data.records.[2].pointsToAdd").value("0.0"))
                .andExpect(jsonPath("$.data.records.[2].pointsAdded").value("0.0"))
                .andExpect(jsonPath("$.data.records.[2].userId").value("5554787"))
                .andExpect(jsonPath("$.data.records.[3].id").value(fourthRandomUUID))
                .andExpect(jsonPath("$.data.records.[3].username").value("username_test_4"))
                .andExpect(jsonPath("$.data.records.[3].displayName").value("Username_Test_4"))
                .andExpect(jsonPath("$.data.records.[3].pointsToAdd").value("0.0"))
                .andExpect(jsonPath("$.data.records.[3].pointsAdded").value("0.0"))
                .andExpect(jsonPath("$.data.records.[3].userId").value("45461487"));


    }

    @Test
    public void it_should_have_any_user_registered() throws Exception {

        ErrorResponse errorResponse = new ErrorResponse("Any users in our database.", 404, HttpStatus.NOT_FOUND.name());
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(errorResponse));

        when(userService.getAllUsers()).thenReturn(requestResponse);

        requestResponse = userService.getAllUsers();

        assertNotNull(requestResponse);

        mockMvc.perform(get("/v2/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value("Any users in our database."))
                .andExpect(jsonPath("$.data.status").value("404"))
                .andExpect(jsonPath("$.data.error").value("NOT_FOUND"));

    }

    @Test
    public void it_should_throw_not_found_exception() throws Exception {

        when(userService.getAllUsers()).thenThrow(new NoSuchElementFoundException("Any users in our database."));
        assertThrows(NoSuchElementFoundException.class, () -> userService.getAllUsers());

        mockMvc.perform(get("/v2/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void it_should_throw_internal_error_users() throws Exception {
        String[] realUsers = new String[]{"161920081", "528689231", "47115827", "36413513"};
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiefsdfds", "zvinniie", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisafsdfds", "Hiromisak", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilvafsdfds", "TheAlbertSilva", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalllfsdfds", "MrFalll", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);
        when(userService.fetchAllUsers()).thenThrow(new InternalServerException("An error occured"));
        assertThrows(InternalServerException.class, () -> userService.fetchAllUsers());


        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(null);

        mockMvc.perform(put("/v2/user/fetch")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void it_should_fetch_users() throws Exception {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        Random random = new Random();
        String firstRandomId = String.valueOf(random.nextInt(50000));
        String secondRandomId = String.valueOf(random.nextInt(50000));
        String thirdRandomId = String.valueOf(random.nextInt(50000));
        String fourthRandomId = String.valueOf(random.nextInt(50000));
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(firstRandomId), "SASDSAusername_test_1", "Username_Test_1", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(secondRandomId), "DSADASusername_test_2", "Username_Test_2", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(thirdRandomId), "DASDSAusername_test_3", "Username_Test_3", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(fourthRandomId), "DSDASDusername_test_4", "Username_Test_4", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        TwitchUserModel userModel = createUserModel(firstRandomId, secondRandomId, thirdRandomId, fourthRandomId);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);
        when(userService.getTwitchUsersByIds(anyList())).thenReturn(userModel);

        SuccessResponse successResponse = new SuccessResponse(Collections.emptyList(), "4 users were updated.");
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));

        when(userService.fetchAllUsers()).thenReturn(requestResponse);

        requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(null);

        mockMvc.perform(put("/v2/user/fetch")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("4 users were updated."))
                .andExpect(jsonPath("$.data.recordCount").value("0"));

    }

    @Test
    public void it_should_fetch_only_one_user() throws Exception {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        Random random = new Random();
        String firstRandomId = String.valueOf(random.nextInt(50000));
        String secondRandomId = String.valueOf(random.nextInt(50000));
        String thirdRandomId = String.valueOf(random.nextInt(50000));
        String fourthRandomId = String.valueOf(random.nextInt(50000));
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(firstRandomId), "username_test_1", "Username_Test_1", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(secondRandomId), "username_test_2", "Username_Test_2", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(thirdRandomId), "username_test_3", "Username_Test_3", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(fourthRandomId), "DSDASDusername_test_4", "Username_Test_4", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        TwitchUserModel userModel = createUserModel(firstRandomId, secondRandomId, thirdRandomId, fourthRandomId);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);
        when(userService.getTwitchUsersByIds(anyList())).thenReturn(userModel);

        SuccessResponse successResponse = new SuccessResponse(Collections.emptyList(), "A user was updated.");
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));

        when(userService.fetchAllUsers()).thenReturn(requestResponse);

        requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(null);

        mockMvc.perform(put("/v2/user/fetch")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("A user was updated."))
                .andExpect(jsonPath("$.data.recordCount").value("0"));

    }

    @Test
    public void it_should_fetch_any_users() throws Exception {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        Random random = new Random();
        String firstRandomId = String.valueOf(random.nextInt(50000));
        String secondRandomId = String.valueOf(random.nextInt(50000));
        String thirdRandomId = String.valueOf(random.nextInt(50000));
        String fourthRandomId = String.valueOf(random.nextInt(50000));
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(firstRandomId), "username_test_1", "Username_Test_1", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(secondRandomId), "username_test_2", "Username_Test_2", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(thirdRandomId), "username_test_3", "Username_Test_3", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(fourthRandomId), "username_test_4", "Username_Test_4", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        TwitchUserModel userModel = createUserModel(firstRandomId, secondRandomId, thirdRandomId, fourthRandomId);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);
        when(userService.getTwitchUsersByIds(anyList())).thenReturn(userModel);

        SuccessResponse successResponse = new SuccessResponse(Collections.emptyList(), "Any users were found to be updated.");
        ResponseEntity<Response> requestResponse = ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));

        when(userService.fetchAllUsers()).thenReturn(requestResponse);

        requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(null);

        mockMvc.perform(put("/v2/user/fetch")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("Any users were found to be updated."))
                .andExpect(jsonPath("$.data.recordCount").value("0"));

    }

    private TwitchUserModel createUserModel(String... ids) {
        List<TwitchUserModelData> userModelDataList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            TwitchUserModelData userData = new TwitchUserModelData();
            userData.setLogin("username_test_" + i + 1);
            userData.setDisplay_name("username_test_1" + i + 1);
            userData.setId(ids[i]);
            userModelDataList.add(userData);
        }


        TwitchUserModel userModel = new TwitchUserModel();
        userModel.setData(userModelDataList);

        return userModel;
    }

}
