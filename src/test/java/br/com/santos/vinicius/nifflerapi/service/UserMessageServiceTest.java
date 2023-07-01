package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserMessageEntity;
import br.com.santos.vinicius.nifflerapi.repository.UserMessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserMessageServiceTest {

    @Autowired
    UserMessageService userMessageService;

    @MockBean
    UserMessageRepository userMessageRepository;

    @MockBean
    UserService userService;

    @MockBean
    BlacklistService blacklistService;

    @MockBean
    LastUserMessageService lastUserMessageService;

    @Test
    public void it_should_analyse_message() throws IOException {
        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "MESSAGE TEST");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("INPUT MESSAGE TEST TO BE ANALYZED");
        userMessageDto.setEmotesSent(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

        lastUserMessage = new LastUserMessageEntity(user, "INPUT MESSAGE TEST TO BE ANALYZED");
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        assertEquals(userMessageDto.getMessage(), lastUserMessage.getLastMessage());

    }

    @Test
    public void it_should_analyse_message_with_emotes() throws IOException {
        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "MESSAGE TEST BLI BLI BLI BLI BLI BLI BLI BLI BLI BLI BLI");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("mrfalllhmm INPUT MESSAGE TEST TO BE ANALYZED");
        String position = "emote:0-9";
        userMessageDto.setEmotesSent(List.of(position));
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

        lastUserMessage = new LastUserMessageEntity(user, "mrfalllhmm INPUT MESSAGE TEST TO BE ANALYZED");
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        assertEquals(userMessageDto.getMessage(), lastUserMessage.getLastMessage());

    }

    @Test
    public void it_should_analyse_message_not_sub() throws IOException {
        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "MESSAGE TEST");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("INPUT MESSAGE TEST TO BE ANALYZED");
        userMessageDto.setEmotesSent(Collections.emptyList());
        userMessageDto.setSubscriber(false);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(0);
        userMessageDto.setSubscriptionTier(0);

        userMessageService.messageAnalysis(userMessageDto);

        lastUserMessage = new LastUserMessageEntity(user, "INPUT MESSAGE TEST TO BE ANALYZED");
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        assertEquals(userMessageDto.getMessage(), lastUserMessage.getLastMessage());

    }

    @Test
    public void it_should_not_analyse_message_user_in_blacklist() throws IOException {
        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(true);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("INPUT MESSAGE TEST TO BE ANALYZED");
        userMessageDto.setEmotesSent(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(null);
        LastUserMessageEntity entity = lastUserMessageService.findUserLastMessage(user);

        assertNull(entity);

    }

    @Test
    public void it_should_not_analyse_message_user_repeated_message() throws IOException {

        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "MESSAGE TEST");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

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

        userMessageService.messageAnalysis(userMessageDto);

        assertEquals(userMessageDto.getMessage(), lastUserMessage.getLastMessage());

    }

    @Test
    public void it_should_not_analyse_message_too_much_Ks() throws IOException {

        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "MESSAGE TEST");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        userMessageDto.setEmotesSent(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

        assertNotEquals(userMessageDto.getMessage(), lastUserMessage.getLastMessage());

    }

    @Test
    public void it_should_analyse_message_user_set_entity() throws IOException {

        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "TESTE");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("Message test input");
        userMessageDto.setEmotesSent(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        UserMessageEntity userMessage = new UserMessageEntity();
        userMessage.setMessageLength(50);
        userMessage.setId(55488L);
        userMessage.setCreatedAt(new Date());
        userMessage.setSpam(false);

        when(userMessageRepository.save(any(UserMessageEntity.class))).thenReturn(userMessage);

        userMessageService.messageAnalysis(userMessageDto);

        assertNotEquals(userMessageDto.getMessage(), lastUserMessage.getLastMessage());

    }

    @Test
    public void it_should_analyse_message_only_emotes() throws IOException {

        UserEntity user = new UserEntity();
        user.setId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "TESTE");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(any(UserEntity.class))).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("mrfalllhm");
        userMessageDto.setEmotesSent(List.of("emoteghdg:0-9"));
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(true);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

        assertNotEquals(userMessageDto.getMessage(), lastUserMessage.getLastMessage());
        assertTrue(userMessageDto.isEmoteOnly());
        assertFalse(userMessageDto.getEmotesSent().isEmpty());

    }


}
