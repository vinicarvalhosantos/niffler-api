package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.repository.LastUserMessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(LastUserMessageService.class)
public class LastUserMessageServiceTest {

    @Autowired
    LastUserMessageService lastUserMessageService;

    @MockBean
    LastUserMessageRepository lastUserMessageRepository;

    @Test
    public void it_should_have_any_last_user_message() {

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(55478L, "");

        when(lastUserMessageRepository.findLastUserMessageByUserId(anyLong())).thenReturn(Optional.empty());
        when(lastUserMessageRepository.save(any(LastUserMessageEntity.class))).thenReturn(lastUserMessage);

        LastUserMessageEntity response = lastUserMessageService.findUserLastMessage(55478L);

        assertNotNull(response);
        assertEquals(lastUserMessage.getUserId(), response.getUserId());
    }

    @Test
    public void it_should_have_a_last_user_message() {

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(55478L, "MESSAGE TEST");

        when(lastUserMessageRepository.findLastUserMessageByUserId(anyLong())).thenReturn(Optional.of(lastUserMessage));

        LastUserMessageEntity response = lastUserMessageService.findUserLastMessage(55478L);

        assertNotNull(response);
        assertEquals(lastUserMessage.getUserId(), response.getUserId());
        assertEquals(lastUserMessage.getLastMessage(), response.getLastMessage());
    }

    @Test
    public void it_should_update_last_user_message() {

        LastUserMessageEntity lastUserMessageNew = new LastUserMessageEntity();
        lastUserMessageNew.setUserId(55478L);
        lastUserMessageNew.setLastMessage("NEW MESSAGE TEST");
        LastUserMessageEntity lastUserMessageOld = new LastUserMessageEntity(55478L, "OLD OLD OLD");

        when(lastUserMessageRepository.save(any(LastUserMessageEntity.class))).thenReturn(lastUserMessageNew);

        lastUserMessageService.updateUserLastMessage(lastUserMessageOld, "NEW MESSAGE TEST");

        when(lastUserMessageRepository.findLastUserMessageByUserId(anyLong())).thenReturn(Optional.of(lastUserMessageNew));
        Optional<LastUserMessageEntity> actualLastMessage = lastUserMessageRepository.findLastUserMessageByUserId(55478L);

        assertTrue(actualLastMessage.isPresent());
        LastUserMessageEntity entity = actualLastMessage.get();
        assertEquals("NEW MESSAGE TEST", entity.getLastMessage());
    }

}
