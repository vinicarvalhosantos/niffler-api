package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.constant.PointsConstants;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserMessageEntity;
import br.com.santos.vinicius.nifflerapi.repository.UserMessageRepository;
import br.com.santos.vinicius.nifflerapi.service.BlacklistService;
import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import br.com.santos.vinicius.nifflerapi.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    private static final int[] SUBSCRIPTION_PERCENTAGES = {
            PointsConstants.PERCENTAGE_FOR_SUBS_T1,
            PointsConstants.PERCENTAGE_FOR_SUBS_T2,
            PointsConstants.PERCENTAGE_FOR_SUBS_T3
    };

    @Override
    public void messageAnalysis(UserMessageDto userMessageDto) {
        log.info("Checking if user is in blacklist.");
        if (blacklistService.isUserInBlacklist(userMessageDto.getUserId())) {
            log.info("User in blacklist. His message will not be analyzed.");
            return;
        }
        log.info("User is not in blacklist! Proceeding.");

        log.info("Starting to fetch user from user message.");
        UserEntity user = userService.fetchFromUserMessage(userMessageDto);

        Long userId = user.getUserId();
        String message = userMessageDto.getMessage();
        int messageLength = userMessageDto.messageLength();
        BigDecimal pointsToAdd = BigDecimal.ZERO;

        LastUserMessageEntity lastUserMessage = lastUserMessageService.findUserLastMessage(userId);

        double similarity = lastUserMessage.compareMessages(message);

        if (StringUtil.isSpam(message) || similarity >= 0.7) {
            lastUserMessageService.updateUserLastMessage(lastUserMessage, message);
            UserMessageEntity userMessage = new UserMessageEntity(userId, messageLength, pointsToAdd, true);
            userMessageRepository.save(userMessage);
            return;
        }

        pointsToAdd = calculatePointsToAdd(userMessageDto, messageLength);

        UserMessageEntity userMessage = new UserMessageEntity(user.getUserId(), messageLength, pointsToAdd, false);
        userMessageRepository.save(userMessage);
        user.setPointsToAdd(pointsToAdd.doubleValue());
        lastUserMessageService.updateUserLastMessage(lastUserMessage, message);
        userService.saveUser(user);
    }

    private BigDecimal calculatePointsToAdd(UserMessageDto userMessageDto, int messageLength) {

        int percentage = calculatePercentage(userMessageDto.isSubscriber(),
                userMessageDto.getSubscriptionTime(), userMessageDto.getSubscriptionTier());

        double pointsToAdd = (Double.parseDouble(String.valueOf(messageLength)) / percentage);

        return BigDecimal.valueOf(pointsToAdd).setScale(2, RoundingMode.HALF_EVEN);
    }

    private int calculatePercentage(boolean isSubscriber, int subscriptionTime, int subscriptionTier) {
        if (isSubscriber) {
            int subtractPercentage = subscriptionTime / 10;
            return SUBSCRIPTION_PERCENTAGES[subscriptionTier - 1] - subtractPercentage;
        }

        return PointsConstants.PERCENTAGE_FOR_NON_SUBS;
    }
}
