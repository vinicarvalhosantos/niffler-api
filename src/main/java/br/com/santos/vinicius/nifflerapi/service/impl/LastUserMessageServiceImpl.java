package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.repository.LastUserMessageRepository;
import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class LastUserMessageServiceImpl implements LastUserMessageService {

    @Autowired
    LastUserMessageRepository lastUserMessageRepository;

    @Override
    public LastUserMessageEntity findUserLastMessage(Long userId) {
        log.info("Looking for users last message.");
        Optional<LastUserMessageEntity> lastUserMessageEntity = lastUserMessageRepository.findLastUserMessageByUserId(userId);

        if (lastUserMessageEntity.isEmpty()) {
            log.info("User has no last message in this channel. Creating one empty.");
            LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(userId, "");
            return lastUserMessageRepository.save(lastUserMessage);
        }

        log.info("Found user last message.");
        return lastUserMessageEntity.get();

    }

    @Override
    public void updateUserLastMessage(LastUserMessageEntity lastUserMessage, String newMessage) {
        log.info("Updating last user message.");
        lastUserMessage.setLastMessage(newMessage);
        lastUserMessageRepository.save(lastUserMessage);
    }


}
