package br.com.santos.vinicius.nifflerapi.model.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@WebMvcTest(UserMessageEntity.class)
public class UserMessageEntityTest {

    @Test
    public void it_should_test_getters() {
        UserMessageEntity userMessage = new UserMessageEntity(55448L, 50,
                new BigDecimal("50"), false);
        Date createdAt = new Date();
        String uuid = UUID.randomUUID().toString();
        userMessage.setCreatedAt(createdAt);
        userMessage.setId(uuid);

        assertEquals(55448L, userMessage.getUserId().longValue());
        assertEquals(50, userMessage.getMessageLength());
        assertEquals(new BigDecimal("50"), userMessage.getPointsToAdd());
        assertFalse(userMessage.isSpam());
        assertEquals(uuid, userMessage.getId());
        assertEquals(createdAt, userMessage.getCreatedAt());

    }

}
