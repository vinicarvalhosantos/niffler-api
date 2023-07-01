package br.com.santos.vinicius.nifflerapi.model.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserMessageEntityTest {

    @Test
    public void it_should_test_getters() {
        UserEntity user = new UserEntity(55448L, "zvinniie", "zvinniie", BigDecimal.ZERO, BigDecimal.ZERO);
        UserMessageEntity userMessage = new UserMessageEntity(user, 50,
                new BigDecimal("50"), false);
        Date createdAt = new Date();
        userMessage.setCreatedAt(createdAt);
        userMessage.setId(1L);

        assertEquals(55448L, userMessage.getId().longValue());
        assertEquals(50, userMessage.getMessageLength());
        assertEquals(new BigDecimal("50"), userMessage.getPointsToAdd());
        assertFalse(userMessage.isSpam());
        assertEquals(createdAt, userMessage.getCreatedAt());

    }

}
