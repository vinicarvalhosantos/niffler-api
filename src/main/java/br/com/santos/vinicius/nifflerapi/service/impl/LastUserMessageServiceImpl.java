package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.repository.LastUserMessageRepository;
import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LastUserMessageServiceImpl implements LastUserMessageService {

    @Autowired
    LastUserMessageRepository lastUserMessageRepository;

    @Override
    public LastUserMessageEntity findUserLastMessage(Long userId) {
        Optional<LastUserMessageEntity> lastUserMessageEntity = lastUserMessageRepository.findLastUserMessageByUserId(userId);

        if (lastUserMessageEntity.isEmpty()) {
            LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(userId, "");
            return lastUserMessageRepository.save(lastUserMessage);
        }

        return lastUserMessageEntity.get();

    }

    @Override
    public void updateUserLastMessage(LastUserMessageEntity lastUserMessage, String newMessage) {
        lastUserMessage.setLastMessage(newMessage);
        lastUserMessageRepository.save(lastUserMessage);
    }


}
