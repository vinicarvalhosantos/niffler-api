package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.exception.ElementAlreadyReportedException;
import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
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
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();
        Date createdAt = new Date();

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(24L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

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

    @Test(expected = NoSuchElementFoundException.class)
    public void it_should_NOT_add_user_in_blacklist_user_does_not_exists() throws Exception {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        Date createdAt = new Date();

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(245L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

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

        blacklistService.addUserInBlacklist(blacklistDtoRequest);

    }

    @Test
    public void it_should_update_user_in_blacklist_when_user_change_username() throws Exception {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);

        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        Date createdAt = new Date();

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(245L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

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

    @Test(expected = ElementAlreadyReportedException.class)
    public void it_should_NOT_ad_user_in_blacklist_when_user_ALREADY_in_blacklist() throws Exception {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();
        Date createdAt = new Date();

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(2455L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

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

        blacklistService.addUserInBlacklist(blacklistDtoRequest);
    }

    @Test
    public void it_should_get_user_from_blacklist_by_username() {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();
        Date createdAt = new Date();

        String input = "username_test";

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(2255L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.of(blacklistEntityExpected));

        ResponseEntity<Response> requestResponse = blacklistService.getUserInBlacklistByUsername(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test(expected = NoSuchElementFoundException.class)
    public void it_should_not_found_user_from_blacklist_by_username() {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        String randomUUID = UUID.randomUUID().toString();
        Date createdAt = new Date();

        String input = "username_test";

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(2445L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        blacklistService.getUserInBlacklistByUsername(input);
    }

    @Test
    public void it_should_get_user_from_blacklist_by_userid() {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();
        Date createdAt = new Date();

        Long input = 55448L;

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(22454L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(blacklistEntityExpected));

        ResponseEntity<Response> requestResponse = blacklistService.getUserInBlacklistByUserId(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test(expected = NoSuchElementFoundException.class)
    public void it_should_not_found_user_from_blacklist_by_userid() {
        UserEntity user = new UserEntity(55547L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();
        Date createdAt = new Date();

        Long input = 55547L;

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(221445L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUserId(any(Long.class))).thenReturn(Optional.empty());

        blacklistService.getUserInBlacklistByUserId(input);
    }

    @Test
    public void it_should_delete_user_from_blacklist_by_username() {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        Date createdAt = new Date();

        String input = "username_test";

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(224547L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUsername(any(String.class))).thenReturn(Optional.of(blacklistEntityExpected));

        ResponseEntity<Response> requestResponse = blacklistService.removeUserFromBlacklistByUsername(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.NO_CONTENT, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_delete_user_from_blacklist_by_userid() {
        UserEntity user = new UserEntity(55547L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        Date createdAt = new Date();

        Long input = 55547L;

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(52454L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        ResponseEntity<Response> requestResponse = blacklistService.removeUserFromBlacklistByUserId(input);

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.NO_CONTENT, requestResponse.getStatusCode());
    }

    @Test
    public void it_should_get_all_users_from_blacklist() {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();
        List<BlacklistEntity> blacklistEntities = new ArrayList<>();

        Date createdAt = new Date();

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(2254L);
        blacklistEntityExpected.setUsername("username_test_1");
        blacklistEntityExpected.setCreatedAt(createdAt);
        blacklistEntities.add(blacklistEntityExpected);

        UserEntity user2 = new UserEntity(555487L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);

        blacklistEntityExpected = new BlacklistEntity();
        blacklistEntityExpected.setUser(user2);
        blacklistEntityExpected.setId(5478L);
        blacklistEntityExpected.setUsername("username_test_2");
        blacklistEntityExpected.setCreatedAt(createdAt);
        blacklistEntities.add(blacklistEntityExpected);

        UserEntity user3 = new UserEntity(5554787L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        blacklistEntityExpected = new BlacklistEntity();
        blacklistEntityExpected.setUser(user3);
        blacklistEntityExpected.setId(87897L);
        blacklistEntityExpected.setUsername("username_test_3");
        blacklistEntityExpected.setCreatedAt(createdAt);
        blacklistEntities.add(blacklistEntityExpected);

        when(blacklistRepository.findAll()).thenReturn(blacklistEntities);

        ResponseEntity<Response> requestResponse = blacklistService.getAllUsersInBlacklist();

        assertNotNull(requestResponse);
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());
    }

    @Test(expected = NoSuchElementFoundException.class)
    public void it_should_not_found_when_get_all_users_from_blacklist() {

        when(blacklistRepository.findAll()).thenReturn(new ArrayList<>());

        blacklistService.getAllUsersInBlacklist();
    }

    @Test
    public void it_should_get_user_from_blacklist() {
        UserEntity user = new UserEntity(55448L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        Date createdAt = new Date();

        Long input = 55448L;

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(5454L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(blacklistEntityExpected));

        boolean response = blacklistService.isUserInBlacklist(input);

        assertTrue(response);
    }

    @Test
    public void it_should_get_user_from_blacklist_false() {
        UserEntity user = new UserEntity(55547L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        BlacklistEntity blacklistEntityExpected = new BlacklistEntity();

        Date createdAt = new Date();

        Long input = 55547L;

        blacklistEntityExpected.setUser(user);
        blacklistEntityExpected.setId(55487L);
        blacklistEntityExpected.setUsername("username_test");
        blacklistEntityExpected.setCreatedAt(createdAt);

        when(blacklistRepository.findByUserId(any(Long.class))).thenReturn(Optional.empty());

        boolean response = blacklistService.isUserInBlacklist(input);

        assertFalse(response);
    }

}
