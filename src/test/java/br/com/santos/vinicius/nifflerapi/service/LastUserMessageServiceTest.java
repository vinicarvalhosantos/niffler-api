package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.repository.LastUserMessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class LastUserMessageServiceTest {

    @Autowired
    LastUserMessageService lastUserMessageService;

    @MockBean
    LastUserMessageRepository lastUserMessageRepository;

    @Test
    public void it_should_have_any_last_user_message() {
        UserEntity user = new UserEntity(55547L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "");

        when(lastUserMessageRepository.findLastUserMessageByUserId(anyLong())).thenReturn(Optional.empty());
        when(lastUserMessageRepository.save(any(LastUserMessageEntity.class))).thenReturn(lastUserMessage);

        LastUserMessageEntity response = lastUserMessageService.findUserLastMessage(user);

        assertNotNull(response);
        assertEquals(lastUserMessage.getId(), response.getId());
    }

    @Test
    public void it_should_have_a_last_user_message() {
        UserEntity user = new UserEntity(55547L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "MESSAGE TEST");

        when(lastUserMessageRepository.findLastUserMessageByUserId(anyLong())).thenReturn(Optional.of(lastUserMessage));

        LastUserMessageEntity response = lastUserMessageService.findUserLastMessage(user);

        assertNotNull(response);
        assertEquals(lastUserMessage.getId(), response.getId());
        assertEquals(lastUserMessage.getLastMessage(), response.getLastMessage());
    }

    @Test
    public void it_should_update_last_user_message() {
        UserEntity user = new UserEntity(55547L, "username_test", "username_test", BigDecimal.ZERO, BigDecimal.ZERO);
        LastUserMessageEntity lastUserMessageNew = new LastUserMessageEntity();
        lastUserMessageNew.setUser(user);
        lastUserMessageNew.setLastMessage("NEW MESSAGE TEST");
        LastUserMessageEntity lastUserMessageOld = new LastUserMessageEntity(user, "OLD OLD OLD");

        when(lastUserMessageRepository.save(any(LastUserMessageEntity.class))).thenReturn(lastUserMessageNew);

        lastUserMessageService.updateUserLastMessage(lastUserMessageOld, "NEW MESSAGE TEST");

        when(lastUserMessageRepository.findLastUserMessageByUserId(anyLong())).thenReturn(Optional.of(lastUserMessageNew));
        Optional<LastUserMessageEntity> actualLastMessage = lastUserMessageRepository.findLastUserMessageByUserId(55478L);

        assertTrue(actualLastMessage.isPresent());
        LastUserMessageEntity entity = actualLastMessage.get();
        assertEquals("NEW MESSAGE TEST", entity.getLastMessage());
    }

}
