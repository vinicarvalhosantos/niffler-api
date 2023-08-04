package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.constant.PointsConstant;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserMessageEntity;
import br.com.santos.vinicius.nifflerapi.repository.UserMessageRepository;
import br.com.santos.vinicius.nifflerapi.runnable.ClearOldMessagePointsRunnable;
import br.com.santos.vinicius.nifflerapi.runnable.RunnableExecutor;
import br.com.santos.vinicius.nifflerapi.service.BlacklistService;
import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import br.com.santos.vinicius.nifflerapi.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    UserMessageRepository userMessageRepository;

    @Autowired
    UserService userService;

    @Autowired
    BlacklistService blacklistService;

    @Autowired
    LastUserMessageService lastUserMessageService;

    @Autowired
    RunnableExecutor runnableExecutor;

    private static final int[] SUBSCRIPTION_PERCENTAGES = {
            PointsConstant.PERCENTAGE_FOR_SUBS_T1,
            PointsConstant.PERCENTAGE_FOR_SUBS_T2,
            PointsConstant.PERCENTAGE_FOR_SUBS_T3
    };

    @Override
    @Transactional
    public void messageAnalysis(UserMessageDto userMessageDto) {
        log.info("Checking if user is in blacklist.");
        if (blacklistService.isUserInBlacklist(userMessageDto.getUserId())) {
            log.info("User in blacklist. His message will not be analyzed.");
            return;
        }
        log.info("User is not in blacklist! Proceeding.");

        UserEntity user = userService.fetchFromUserMessage(userMessageDto);

        String message = userMessageDto.getMessage();
        int messageLength = userMessageDto.messageLength();
        BigDecimal pointsToAdd = BigDecimal.ZERO;

        LastUserMessageEntity lastUserMessage = lastUserMessageService.findUserLastMessage(user);

        log.info("Comparing the messages.");
        double similarity = lastUserMessage.compareMessages(message);

        if (StringUtil.isSpam(message) || similarity >= 0.7) {
            log.info("The message sent is a spam, user will not receive points this message.");

            UserMessageEntity userMessage = new UserMessageEntity(user, messageLength, pointsToAdd, true, userMessageDto.getMessageId());
            saveData(user, userMessage, lastUserMessage, pointsToAdd, message);
            return;
        }

        pointsToAdd = calculatePointsToAdd(userMessageDto, messageLength);
        UserMessageEntity userMessage = new UserMessageEntity(user, messageLength, pointsToAdd, false, userMessageDto.getMessageId());

        saveData(user, userMessage, lastUserMessage, pointsToAdd, message);
    }

    @Override
    public void deleteUserMessage(String messageId) {
        log.info("Finding user message to be deleted. Message Id: {}", messageId);
        Optional<UserMessageEntity> optionalUserMessage = this.userMessageRepository.findByMessageId(messageId);

        if (optionalUserMessage.isEmpty()) {
            log.info("Was not possible to find the message.");
            return;
        }

        UserMessageEntity messageEntity = optionalUserMessage.get();
        UserEntity user = messageEntity.getUser();
        messageEntity.setDeleted(true);
        BigDecimal pointsToBeRemoved = messageEntity.getPointsToAdd();
        log.info("{} will be substracted from users points", pointsToBeRemoved);
        BigDecimal usersPoints = user.getPointsToAdd();
        user.setPointsToAdd(usersPoints.subtract(pointsToBeRemoved, new MathContext(10)));

        userMessageRepository.save(messageEntity);
        userService.saveUser(user);
    }

    @Override
    public void deleteUserLastMessage(Long userId) {
        log.info("Looking for user last message.");

        Optional<UserMessageEntity> optionalUserMessage = this.userMessageRepository
                .findFirstByUserIdAndSpamIsFalseAndDeletedIsFalse(userId, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (optionalUserMessage.isEmpty()) {
            log.info("Was not possible to find user last message.");
            return;
        }

        String messageId = optionalUserMessage.get().getMessageId();
        deleteUserMessage(messageId);
    }

    @Override
    @Transactional
    public void clearOldMessagePoints() {
        try {
            log.info("Checking if there are message points to be deleted.");
            List<UserMessageEntity> oldUserMessages = this.userMessageRepository.findAllOldMessages();
            if (oldUserMessages.isEmpty()) {
                log.info("Any old messages was found!");
                return;
            }
            log.info("Clearing all old messages.");
            ClearOldMessagePointsRunnable oldMessagePointsRunnable = new ClearOldMessagePointsRunnable(oldUserMessages, userMessageRepository);
            runnableExecutor.execute(oldMessagePointsRunnable);
            log.info("Cleared all old messages.");
        } catch (InterruptedException ex) {
            log.error("Was not possible to clear the old message points. {}", ex.getMessage());
        }
    }

    private void saveData(UserEntity user, UserMessageEntity userMessage, LastUserMessageEntity lastUserMessage,
                          BigDecimal pointsToAdd, String message) {
        log.info("Finishing and saving the message and points information.");

        try {
            userMessageRepository.save(userMessage);
            lastUserMessageService.updateUserLastMessage(lastUserMessage, message);
            user.setPointsToAdd(pointsToAdd.add(user.getPointsToAdd()));

            if (!userMessage.isSpam())
                userService.saveUser(user);
        } catch (Exception ex) {
            log.error("Was not possible to save the message and points information. {}", ex.getMessage());
        }

    }

    private BigDecimal calculatePointsToAdd(UserMessageDto userMessageDto, int messageLength) {
        log.info("Starting to calculate the percentage that user will receive with this message.");

        int percentage = calculatePercentage(userMessageDto.isSubscriber(),
                userMessageDto.getSubscriptionTime(), userMessageDto.getSubscriptionTier());

        double pointsToAdd = (Double.parseDouble(String.valueOf(messageLength)) / percentage);
        log.info("Checking if it is user's first message.");
        if (userMessageDto.isFirstMessage()) {
            log.info("User's first message! Receiving a bonus.");
            pointsToAdd += 10;
        }

        log.info("Checking if user's  message is polite.");
        if (userMessageDto.isMessagePolite()) {
            log.info("User's message is polite! Receiving a bonus.");
            pointsToAdd = pointsToAdd * 0.25;
        }

        log.info("Finished calculation.");
        return BigDecimal.valueOf(pointsToAdd).setScale(2, RoundingMode.HALF_EVEN);
    }

    private int calculatePercentage(boolean isSubscriber, int subscriptionTime, int subscriptionTier) {
        if (isSubscriber) {
            int subtractPercentage = subscriptionTime < 200 ? subscriptionTime / 10 : 20;
            return SUBSCRIPTION_PERCENTAGES[subscriptionTier - 1] - subtractPercentage;
        }

        return PointsConstant.PERCENTAGE_FOR_NON_SUBS;
    }
}
