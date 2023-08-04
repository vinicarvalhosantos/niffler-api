package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.repository.LastUserMessageRepository;
import br.com.santos.vinicius.nifflerapi.runnable.ClearLastUserMessageRunnable;
import br.com.santos.vinicius.nifflerapi.runnable.RunnableExecutor;
import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LastUserMessageServiceImpl implements LastUserMessageService {

    @Autowired
    LastUserMessageRepository lastUserMessageRepository;

    @Autowired
    RunnableExecutor runnableExecutor;

    @Override
    public LastUserMessageEntity findUserLastMessage(UserEntity user) {
        log.info("Looking for users last message.");
        Optional<LastUserMessageEntity> lastUserMessageEntity = lastUserMessageRepository.findLastUserMessageByUserId(user.getId());

        if (lastUserMessageEntity.isEmpty()) {
            log.info("User has no last message in this channel. Creating one empty.");
            LastUserMessageEntity lastUserMessage = new LastUserMessageEntity(user, "");
            return lastUserMessageRepository.save(lastUserMessage);
        }

        log.info("Found user last message.");
        return lastUserMessageEntity.get();

    }

    @Override
    public void updateUserLastMessage(LastUserMessageEntity lastUserMessage, String newMessage) {
        log.info("Updating last user message.");
        lastUserMessage.setLastMessage(newMessage);
        if (!lastUserMessage.getLastMessageAt().isEqual(LocalDate.now())) {
            lastUserMessage.setLastMessageAt(LocalDate.now());
        }
        lastUserMessageRepository.save(lastUserMessage);
    }

    @Override
    public void clearLastUserMessages() {
        try {
            log.info("Checking if there are last user messages to be cleared!");
            List<LastUserMessageEntity> lastUserMessageEntities = this.lastUserMessageRepository.findAll();
            lastUserMessageEntities = lastUserMessageEntities.stream()
                    .filter(lastUserMessageEntity -> !StringUtils.isEmpty(lastUserMessageEntity.getLastMessage())
                            && lastUserMessageEntity.getLastMessageAt().isBefore(LocalDate.now()))
                    .collect(Collectors.toList());
            if (lastUserMessageEntities.isEmpty()) {
                log.info("Any messages were found!");
                return;
            }

            lastUserMessageEntities.forEach(lastUserMessageEntity -> lastUserMessageEntity.setLastMessage(StringUtils.EMPTY));

            log.info("Clearing all the last users messages.");
            ClearLastUserMessageRunnable clearLastUserMessageRunnable = new ClearLastUserMessageRunnable(lastUserMessageEntities, lastUserMessageRepository);
            runnableExecutor.execute(clearLastUserMessageRunnable);
            log.info("All the last users messages cleared.");
        } catch (InterruptedException ex) {
            log.error("Was not possible to clear the users last messages. {}", ex.getMessage());
        }
    }


}
