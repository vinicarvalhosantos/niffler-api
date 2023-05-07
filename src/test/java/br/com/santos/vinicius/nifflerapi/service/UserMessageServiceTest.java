package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.repository.UserMessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(UserMessageService.class)
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
        user.setUserId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(55488L, "MESSAGE TEST");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(anyLong())).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("INPUT MESSAGE TEST TO BE ANALYZED");
        userMessageDto.setEmotes(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

    }

    @Test
    public void it_should_analyse_message_with_emotes() throws IOException {
        UserEntity user = new UserEntity();
        user.setUserId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(55488L, "MESSAGE TEST BLI BLI BLI BLI BLI BLI BLI BLI BLI BLI BLI");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(anyLong())).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("mrfalllhmm INPUT MESSAGE TEST TO BE ANALYZED");
        String position = "emote:0-9";
        userMessageDto.setEmotes(List.of(position));
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

    }

    @Test
    public void it_should_analyse_message_not_sub() throws IOException {
        UserEntity user = new UserEntity();
        user.setUserId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(55488L, "MESSAGE TEST");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(anyLong())).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("INPUT MESSAGE TEST TO BE ANALYZED");
        userMessageDto.setEmotes(Collections.emptyList());
        userMessageDto.setSubscriber(false);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(0);
        userMessageDto.setSubscriptionTier(0);

        userMessageService.messageAnalysis(userMessageDto);

    }

    @Test
    public void it_should_not_analyse_message_user_in_blacklist() throws IOException {

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(true);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("INPUT MESSAGE TEST TO BE ANALYZED");
        userMessageDto.setEmotes(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

    }

    @Test
    public void it_should_not_analyse_message_user_repeated_message() throws IOException {

        UserEntity user = new UserEntity();
        user.setUserId(55488L);
        user.setUsername("zvinniie");
        user.setDisplayName("zvinniie");

        LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(55488L, "MESSAGE TEST");

        when(blacklistService.isUserInBlacklist(anyLong())).thenReturn(false);
        when(userService.fetchFromUserMessage(any(UserMessageDto.class))).thenReturn(user);
        when(lastUserMessageService.findUserLastMessage(anyLong())).thenReturn(lastUserMessage);

        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setMessage("MESSAGE TEST");
        userMessageDto.setEmotes(Collections.emptyList());
        userMessageDto.setSubscriber(true);
        userMessageDto.setEmoteOnly(false);
        userMessageDto.setUsername("zvinniie");
        userMessageDto.setDisplayName("zvinniie");
        userMessageDto.setUserId(55488L);
        userMessageDto.setSubscriptionTime(15);
        userMessageDto.setSubscriptionTier(1);

        userMessageService.messageAnalysis(userMessageDto);

    }


}
