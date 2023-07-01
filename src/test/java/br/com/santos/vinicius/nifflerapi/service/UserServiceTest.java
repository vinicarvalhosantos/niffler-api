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
import java.math.BigDecimal;
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
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(55547L, "username_test_1", "Username_Test_1", BigDecimal.ZERO, BigDecimal.ZERO);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(555487L, "username_test_2", "Username_Test_2", BigDecimal.ZERO, BigDecimal.ZERO);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(5554787L, "username_test_3", "Username_Test_3", BigDecimal.ZERO, BigDecimal.ZERO);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(45461487L, "username_test_4", "Username_Test_4", BigDecimal.ZERO, BigDecimal.ZERO);
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
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiefsdfds", "zvinniiegfdghfdgf", BigDecimal.ZERO, BigDecimal.ZERO);
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisafsdfds", "Hiromisakgdfgfgf", BigDecimal.ZERO, BigDecimal.ZERO);
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilvafsdfds", "TheAlbertSilvagfdgfd", BigDecimal.ZERO, BigDecimal.ZERO);
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalllfsdfds", "MrFalll", BigDecimal.ZERO, BigDecimal.ZERO);
        fourthUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_fetch_any_users() throws IOException, InterruptedException {
        
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniie", "zvinniie", BigDecimal.ZERO, BigDecimal.ZERO);
        
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisak", "Hiromisak", BigDecimal.ZERO, BigDecimal.ZERO);
        
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilva", "TheAlbertSilva", BigDecimal.ZERO, BigDecimal.ZERO);
        
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalll", "MrFalll", BigDecimal.ZERO, BigDecimal.ZERO);
        
        fourthUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_fetch_only_one_user() throws IOException, InterruptedException {
        
        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiedasdsafd", "zvinniie", BigDecimal.ZERO, BigDecimal.ZERO);
        
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisak", "Hiromisak", BigDecimal.ZERO, BigDecimal.ZERO);
        
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilva", "TheAlbertSilva", BigDecimal.ZERO, BigDecimal.ZERO);
        
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalll", "MrFalll", BigDecimal.ZERO, BigDecimal.ZERO);
        
        fourthUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_fetch_when_user_is_deleted() throws IOException, InterruptedException {

        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiedasdsafd", "zvinniie", BigDecimal.ZERO, BigDecimal.ZERO);
        
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisak", "Hiromisak", BigDecimal.ZERO, BigDecimal.ZERO);
        
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilva", "TheAlbertSilva", BigDecimal.ZERO, BigDecimal.ZERO);
        
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalll", "MrFalll", BigDecimal.ZERO, BigDecimal.ZERO);
        
        fourthUser.setCreatedAt(createdAt);

        UserEntity fivethUser = new UserEntity(Long.parseLong("14545675"), "other_user", "other_user", BigDecimal.ZERO, BigDecimal.ZERO);
        
        fivethUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser, fivethUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_fetch_when_bad_request() throws IOException, InterruptedException {

        Date createdAt = new Date();

        UserEntity firstUser = new UserEntity(Long.parseLong(realUsers[0]), "zvinniiedasdsafd", "zvinniie", BigDecimal.ZERO, BigDecimal.ZERO);
        
        firstUser.setCreatedAt(createdAt);

        UserEntity secondUser = new UserEntity(Long.parseLong(realUsers[1]), "hiromisak", "Hiromisak", BigDecimal.ZERO, BigDecimal.ZERO);
        
        secondUser.setCreatedAt(createdAt);

        UserEntity thirdUser = new UserEntity(Long.parseLong(realUsers[2]), "thealbertsilva", "TheAlbertSilva", BigDecimal.ZERO, BigDecimal.ZERO);
        
        thirdUser.setCreatedAt(createdAt);

        UserEntity fourthUser = new UserEntity(Long.parseLong(realUsers[3]), "mrfalll", "MrFalll", BigDecimal.ZERO, BigDecimal.ZERO);
        
        fourthUser.setCreatedAt(createdAt);

        UserEntity fivethUser = new UserEntity(14445478744L, "other_user", "other_user", BigDecimal.ZERO, BigDecimal.ZERO);

        fivethUser.setCreatedAt(createdAt);

        List<UserEntity> users = List.of(firstUser, secondUser, thirdUser, fourthUser, fivethUser);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<Response> requestResponse = userService.fetchAllUsers();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.BAD_REQUEST, requestResponse.getStatusCode());
    }

    @Test
    public void should_fetch_user_by_username() throws IOException {

        Date createdAt = new Date();

        UserEntity userEntity = new UserEntity(161920081L, "zvinniie", "zvinniiesss", BigDecimal.ZERO, BigDecimal.ZERO);

        userEntity.setCreatedAt(createdAt);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        UserEntity response = userService.fetchUserByUsername("zvinniie");

        assertNotNull(response);
        assertEquals(161920081L, response.getId());
    }

    @Test
    public void should_fetch_user_by_username_when_user_not_in_database() throws IOException {

        Date createdAt = new Date();

        UserEntity userEntity = new UserEntity(161920081L, "zvinniie", "zvinniiesss", BigDecimal.ZERO, BigDecimal.ZERO);

        userEntity.setCreatedAt(createdAt);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        UserEntity response = userService.fetchUserByUsername("zvinniie");

        assertNotNull(response);
        assertEquals(161920081L, response.getId());
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
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        when(userRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity response = userService.fetchFromUserMessage(userMessageDto);

        assertNotNull(response);
        assertEquals(response.getId(), userMessageDto.getUserId());
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
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        when(userRepository.findByUserId(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity response = userService.fetchFromUserMessage(userMessageDto);

        assertNotNull(response);
        assertEquals(response.getId(), userMessageDto.getUserId());
    }

    @Test
    public void should_save_user() {

        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity response = userService.saveUser(user);

        assertNotNull(response);
        assertEquals(response.getId(), user.getId());
    }

}
