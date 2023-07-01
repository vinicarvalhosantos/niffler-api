package br.com.santos.vinicius.nifflerapi.model.entity;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModelData;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserEntityTest {

    @Test
    public void it_should_test_twitch_users() {

        UserEntity user = new UserEntity(55448L, "zvinniie", "zvinniie",
                BigDecimal.ZERO, BigDecimal.ZERO);
        TwitchUserModelData twitchUser = new TwitchUserModelData();
        twitchUser.setId("55448");
        twitchUser.setLogin("zvinniie");
        twitchUser.setDisplay_name("zvinniie");

        assertTrue(user.equalsTwitchUser(twitchUser));
        assertTrue(user.equalsTwitchUser(user));
        assertFalse(user.equalsTwitchUser(null));

        user.fetchUserFromTwitchUser(twitchUser);
    }

    @Test
    public void it_should_test_user_message() {

        UserEntity user = new UserEntity(55448L, "zvinniie", "zvinniie",
                BigDecimal.ZERO, BigDecimal.ZERO);
        UserMessageDto userMessage = new UserMessageDto();
        userMessage.setUserId(55448L);
        userMessage.setUsername("zvinniie");
        userMessage.setDisplayName("zvinniie");

        assertTrue(user.equalsUserMessage(userMessage));
        assertTrue(user.equalsUserMessage(user));
        assertFalse(user.equalsUserMessage(null));

        user.fetchUserFromMessage(userMessage);

        user.setDisplayName("testName");
        user.setUsername("testName");

        user.fetchUserFromMessage(userMessage);
        assertEquals("zvinniie", user.getUsername());
        assertEquals("zvinniie", user.getDisplayName());
    }

}
