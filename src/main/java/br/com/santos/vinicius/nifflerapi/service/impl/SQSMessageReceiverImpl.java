package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.service.SQSMessageReceiver;
import br.com.santos.vinicius.nifflerapi.service.UserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class SQSMessageReceiverImpl implements SQSMessageReceiver {

    @Autowired
    UserMessageService userMessageService;

    public void receiveMessage(UserMessageDto message, String senderId) throws IOException {
        log.info("User message received from twitch. ({})", senderId);
        userMessageService.messageAnalysis(message);
    }
}
