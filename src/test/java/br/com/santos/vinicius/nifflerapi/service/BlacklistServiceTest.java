package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.TwitchUserModelData;
import br.com.santos.vinicius.nifflerapi.model.dto.BlacklistDto;
import br.com.santos.vinicius.nifflerapi.model.entity.BlacklistEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.repository.BlacklistRepository;
import br.com.santos.vinicius.nifflerapi.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(BlacklistService.class)
public class BlacklistServiceTest {

    @Autowired
    BlacklistService blacklistService;

    @MockBean
    BlacklistRepository blacklistRepository;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    public void it_should_add_user_in_blacklist() throws Exception {

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

        when(blacklistRepository.save(any(BlacklistEntity.class))).thenReturn(blacklistEntityExpected);
        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userService.fetchUserByUsername(any(String.class))).thenReturn(user);
        when(userService.getTwitchUser(any(String.class))).thenReturn(twitchUser);

        ResponseEntity<Response> requestResponse = blacklistService.addUserInBlacklist(blacklistDtoRequest);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.CREATED, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_NOT_add_user_in_blacklist_user_does_not_exists() throws Exception {

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

        TwitchUserModel twitchUser = new TwitchUserModel();
        twitchUser.setData(null);

        when(blacklistRepository.save(any(BlacklistEntity.class))).thenReturn(blacklistEntityExpected);
        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userService.fetchUserByUsername(any(String.class))).thenReturn(null);
        when(userService.getTwitchUser(any(String.class))).thenReturn(twitchUser);

        ResponseEntity<Response> requestResponse = blacklistService.addUserInBlacklist(blacklistDtoRequest);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.NOT_FOUND, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_update_user_in_blacklist_when_user_change_username() throws Exception {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        String randomUserUUID = UUID.randomUUID().toString();

        UserEntity user = new UserEntity(55547L, "username_test_updated", "username_test", 0.0, 0.0);
        user.setId(randomUserUUID);
        user.setCreatedAt(createdAt);

        BlacklistDto blacklistDtoRequest = new BlacklistDto("username_test");

        TwitchUserModelData data = new TwitchUserModelData();
        data.setId("55547");
        data.setLogin("username_test_updated");
        data.setDisplay_name("username_test_updated");
        TwitchUserModel twitchUser = new TwitchUserModel();
        twitchUser.setData(List.of(data));

        when(blacklistRepository.save(any(BlacklistEntity.class))).thenReturn(blacklistEntityExpected);
        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(blacklistRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(blacklistEntityExpected));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userService.fetchUserByUsername(any(String.class))).thenReturn(user);
        when(userService.getTwitchUser(any(String.class))).thenReturn(twitchUser);

        ResponseEntity<Response> requestResponse = blacklistService.addUserInBlacklist(blacklistDtoRequest);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.CREATED, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_NOT_ad_user_in_blacklist_when_user_ALREADY_in_blacklist() throws Exception {

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

        when(blacklistRepository.save(any(BlacklistEntity.class))).thenReturn(blacklistEntityExpected);
        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(blacklistRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(blacklistEntityExpected));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userService.fetchUserByUsername(any(String.class))).thenReturn(user);
        when(userService.getTwitchUser(any(String.class))).thenReturn(twitchUser);

        ResponseEntity<Response> requestResponse = blacklistService.addUserInBlacklist(blacklistDtoRequest);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.ALREADY_REPORTED, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_get_user_from_blacklist_by_username() {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        String input = "username_test";

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.of(blacklistEntityExpected));

        ResponseEntity<Response> requestResponse = blacklistService.getUserInBlacklistByUsername(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_not_found_user_from_blacklist_by_username() {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        String input = "username_test";

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        ResponseEntity<Response> requestResponse = blacklistService.getUserInBlacklistByUsername(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.NOT_FOUND, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_get_user_from_blacklist_by_userid() {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        Long input = 55547L;

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(blacklistEntityExpected));

        ResponseEntity<Response> requestResponse = blacklistService.getUserInBlacklistByUserId(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_not_found_user_from_blacklist_by_userid() {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        Long input = 55547L;

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUserId(any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<Response> requestResponse = blacklistService.getUserInBlacklistByUserId(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.NOT_FOUND, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_delete_user_from_blacklist_by_username() {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        String input = "username_test";

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.of(blacklistEntityExpected));

        ResponseEntity<Response> requestResponse = blacklistService.removeUserFromBlacklistByUsername(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.NO_CONTENT, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_delete_user_from_blacklist_by_userid() {

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        Long input = 55547L;

        blacklistEntityExpected.setUserId(55547L);
        blacklistEntityExpected.setId(randomUUID);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        ResponseEntity<Response> requestResponse = blacklistService.removeUserFromBlacklistByUserId(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.NO_CONTENT, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_get_all_users_from_blacklist() {

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

        when(blacklistRepository.findAll()).thenReturn(blacklistEntities);

        ResponseEntity<Response> requestResponse = blacklistService.getAllUsersInBlacklist();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_not_found_when_get_all_users_from_blacklist() {

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

        when(blacklistRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<Response> requestResponse = blacklistService.getAllUsersInBlacklist();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.NOT_FOUND, requestResponse.getStatusCode());
    }

}
