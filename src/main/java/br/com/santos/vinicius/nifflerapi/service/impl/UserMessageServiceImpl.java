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
import br.com.santos.vinicius.nifflerapi.singleton.UserCheers;
import br.com.santos.vinicius.nifflerapi.util.StringUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
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

    final UserMessageRepository userMessageRepository;

    final UserService userService;

    final BlacklistService blacklistService;

    final LastUserMessageService lastUserMessageService;

    final RunnableExecutor runnableExecutor;

    private static final int[] SUBSCRIPTION_PERCENTAGES = {
            PointsConstant.PERCENTAGE_FOR_SUBS_T1,
            PointsConstant.PERCENTAGE_FOR_SUBS_T2,
            PointsConstant.PERCENTAGE_FOR_SUBS_T3
    };
    private static final BigDecimal BONUS_FIRST_MESSAGE = BigDecimal.valueOf(0.5);

    private static final BigDecimal BONUS_POLITE_MESSAGE_MULTIPLIER = BigDecimal.valueOf(0.25);

    private static final BigDecimal MAX_POINTS_VALUE = BigDecimal.valueOf(100);

    private static final int SCALE = 2;

    public UserMessageServiceImpl(UserMessageRepository userMessageRepository, UserService userService, BlacklistService blacklistService, LastUserMessageService lastUserMessageService, RunnableExecutor runnableExecutor) {
        Assert.notNull(userMessageRepository, "UserMessageRepository must not be null");
        Assert.notNull(userService, "UserService must not be null");
        Assert.notNull(blacklistService, "BlacklistService must not be null");
        Assert.notNull(lastUserMessageService, "LastUserMessageService must not be null");
        Assert.notNull(runnableExecutor, "RunnableExecutor must not be null");
        this.userMessageRepository = userMessageRepository;
        this.userService = userService;
        this.blacklistService = blacklistService;
        this.lastUserMessageService = lastUserMessageService;
        this.runnableExecutor = runnableExecutor;
    }

    @Override
    @Transactional
    public void messageAnalysis(UserMessageDto userMessageDto) {
        log.info("Checking if user is in blacklist.");
        if (blacklistService.isUserInBlacklist(userMessageDto.getUserId())) {
            log.info("User in blacklist. His message will not be analyzed.");
            return;
        }

        UserEntity user = userService.fetchFromUserMessage(userMessageDto);

        if (user.isDeleted()) {
            log.warn("User is set as deleted. Something is wrong, please check!");
            return;
        }

        String message = userMessageDto.getMessage();
        int messageLength = userMessageDto.messageLength();
        BigDecimal pointsToAdd = BigDecimal.ZERO;

        LastUserMessageEntity lastUserMessage = lastUserMessageService.findUserLastMessage(user);

        log.info("Comparing the messages.");
        double similarity = lastUserMessage.compareMessages(message);

        if (StringUtil.isFlood(message) || similarity >= 0.7) {
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

    @Override
    public void updateDeletedUserMessage(List<UserEntity> userEntityList, UserEntity userDeleted) {
        log.info("Updating all deleted users message to be as deleted user.");
        List<UserMessageEntity> userMessageEntityList = userMessageRepository.findAllByUserIn(userEntityList);
        userMessageEntityList.forEach(userMessage -> userMessage.setUser(userDeleted));

        userMessageRepository.saveAll(userMessageEntityList);
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

        double percentage = calculatePercentage(userMessageDto.isSubscriber(), userMessageDto.getSubscriptionTime(),
                userMessageDto.getSubscriptionTier(), userMessageDto.getCheers(), userMessageDto.getUserId());

        BigDecimal pointsToAdd = BigDecimal.valueOf(messageLength).divide(BigDecimal.valueOf(percentage), SCALE, RoundingMode.HALF_EVEN);

        if (pointsToAdd.compareTo(MAX_POINTS_VALUE) > 0) {
            pointsToAdd = MAX_POINTS_VALUE;
        }

        log.info("Checking if it is user's first message.");
        if (userMessageDto.isFirstMessage()) {
            log.info("User's first message! Receiving a bonus.");
            pointsToAdd = pointsToAdd.multiply(BONUS_FIRST_MESSAGE);
        }

        log.info("Checking if user's  message is polite.");
        if (userMessageDto.isMessagePolite()) {
            log.info("User's message is polite! Receiving a bonus.");
            pointsToAdd = pointsToAdd.multiply(BONUS_POLITE_MESSAGE_MULTIPLIER);
        }

        log.info("Finished calculation.");
        return pointsToAdd;
    }

    private double calculatePercentage(boolean isSubscriber, int subscriptionTime, int subscriptionTier, int cheers, Long userId) {
        if (isSubscriber) {
            return calculateSubscriberPercentage(subscriptionTime, subscriptionTier, cheers, userId);
        }

        return calculateNonSubscriberPercentage(cheers, userId);
    }

    private int calculateSubscriberPercentage(int subscriptionTime, int subscriptionTier, int cheers, Long userId) {
        int subtractPercentage = calculateSubscriberSubtractPercentage(subscriptionTime);
        int subscriptionPercentage = SUBSCRIPTION_PERCENTAGES[subscriptionTier - 1];
        subtractPercentage += calculateCheersSubtractPercentage(cheers, userId);
        if (subtractPercentage >= subscriptionPercentage) return 1;

        return subscriptionPercentage - subtractPercentage;
    }

    private double calculateNonSubscriberPercentage(int cheers, Long userId) {
        double subtractPercentage = calculateCheersSubtractPercentage(cheers, userId);
        if (subtractPercentage >= PointsConstant.PERCENTAGE_FOR_NON_SUBS) return 1;

        return PointsConstant.PERCENTAGE_FOR_NON_SUBS - subtractPercentage;
    }

    private int calculateSubscriberSubtractPercentage(int subscriptionTime) {
        int maxSubscriptionTimeForSubtract = 200;

        return subscriptionTime < maxSubscriptionTimeForSubtract ? subscriptionTime / 10 : 20;
    }

    private double calculateCheersSubtractPercentage(int cheers, Long userId) {
        if (cheers == 0) {
            return 0;
        }

        UserCheers userCheers = UserCheers.getInstance();
        int cheersSent = cheers + userCheers.cheersAmountSentByUser(userId);

        if (cheersSent >= 100) {
            userCheers.clear(userId);
            return (double) cheersSent / 100;
        }

        userCheers.addCheersIntoList(userId, cheers);
        return 0;
    }
}
