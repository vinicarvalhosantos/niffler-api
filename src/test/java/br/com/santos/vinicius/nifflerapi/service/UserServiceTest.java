package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    String[] realUsers = new String[]{"161920081", "528689231", "47115827", "36413513"};


    @Test
    public void it_should_get_all_users() {
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

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.getAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test(expected = NoSuchElementFoundException.class)
    public void it_should_have_any_user_registered() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        userService.getAllUsers();

    }

    @Test
    public void it_should_fetch_users() throws IOException, InterruptedException {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiefsdfds", "zvinniiegfdghfdgf", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisafsdfds", "Hiromisakgdfgfgf", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilvafsdfds", "TheAlbertSilvagfdgfd", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalllfsdfds", "MrFalll", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_fetch_any_users() throws IOException, InterruptedException {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniie", "zvinniie", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisak", "Hiromisak", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilva", "TheAlbertSilva", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalll", "MrFalll", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_fetch_only_one_user() throws IOException, InterruptedException {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiedasdsafd", "zvinniie", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisak", "Hiromisak", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilva", "TheAlbertSilva", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalll", "MrFalll", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_fetch_when_user_is_deleted() throws IOException, InterruptedException {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        String fivethRandomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiedasdsafd", "zvinniie", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisak", "Hiromisak", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilva", "TheAlbertSilva", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalll", "MrFalll", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        UserEntity fivethUser = new UserEntity(Long.parseLong("14545675"), "other_user", "other_user", 0.0, 0.0);
        fivethUser.setId(fivethRandomUUID);
        fivethUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser, fivethUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_fetch_when_bad_request() throws IOException, InterruptedException {
        String firstRandomUUID = UUID.randomUUID().toString();
        String secondRandomUUID = UUID.randomUUID().toString();
        String thirdRandomUUID = UUID.randomUUID().toString();
        String fourthRandomUUID = UUID.randomUUID().toString();
        String fivethRandomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiedasdsafd", "zvinniie", 0.0, 0.0);
        firstUser.setId(firstRandomUUID);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisak", "Hiromisak", 0.0, 0.0);
        secondUser.setId(secondRandomUUID);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilva", "TheAlbertSilva", 0.0, 0.0);
        thirdUser.setId(thirdRandomUUID);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalll", "MrFalll", 0.0, 0.0);
        fourthUser.setId(fourthRandomUUID);
        fourthUser.setCreatedAt(createdAt);

        UserEntity fivethUser = new UserEntity(14445478744L, "other_user", "other_user", 0.0, 0.0);
        fivethUser.setId(fivethRandomUUID);
        fivethUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser, fivethUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.BAD_REQUEST, requestResponse.getStatusCode());
    }

    @Test
    public void should_fetch_user_by_username() throws IOException {
        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity userEntity = new UserEntity(161920081L, "zvinniie", "zvinniiesss", 0.0, 0.0);
        userEntity.setId(randomUUID);
        userEntity.setCreatedAt(createdAt);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        UserEntity response = userService.fetchUserByUsername("zvinniie");

        assertNotNull(response);
        assertEquals(161920081L, response.getUserId());
    }

    @Test
    public void should_fetch_user_by_username_when_user_not_in_database() throws IOException {
        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        UserEntity userEntity = new UserEntity(161920081L, "zvinniie", "zvinniiesss", 0.0, 0.0);
        userEntity.setId(randomUUID);
        userEntity.setCreatedAt(createdAt);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        UserEntity response = userService.fetchUserByUsername("zvinniie");

        assertNotNull(response);
        assertEquals(161920081L, response.getUserId());
    }

    @Test
    public void should_fetch_user_by_username_when_user_does_not_exist() throws IOException {

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        UserEntity response = userService.fetchUserByUsername("user_teste_teste");

        assertNull(response);
    }

    @Test
    public void should_fetch_user_by_user_message_model() {

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("MESSAGE TEST");
        userMessageDto.setEmotesSent(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        UserEntity user = new UserEntity();
        user.setUserId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        when(userRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity response = userService.fetchFromUserMessage(userMessageDto);

        assertNotNull(response);
        assertEquals(response.getUserId(), userMessageDto.getUserId());
    }

    @Test
    public void should_fetch_user_by_user_message_model_when_exists() {

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("MESSAGE TEST");
        userMessageDto.setEmotesSent(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        UserEntity user = new UserEntity();
        user.setUserId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        when(userRepository.findByUserId(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity response = userService.fetchFromUserMessage(userMessageDto);

        assertNotNull(response);
        assertEquals(response.getUserId(), userMessageDto.getUserId());
    }

    @Test
    public void should_save_user() {

        UserEntity user = new UserEntity();
        user.setUserId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity response = userService.saveUser(user);

        assertNotNull(response);
        assertEquals(response.getUserId(), user.getUserId());
    }

}
