package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.constant.PointsConstant;
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
            PointsConstant.PERCENTAGE_FOR_SUBS_T1,
            PointsConstant.PERCENTAGE_FOR_SUBS_T2,
            PointsConstant.PERCENTAGE_FOR_SUBS_T3
    };

    @Override
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
            lastUserMessageService.updateUserLastMessage(lastUserMessage, message);
            UserMessageEntity userMessage = new UserMessageEntity(user, messageLength, pointsToAdd, true);
            userMessageRepository.save(userMessage);
            return;
        }

        pointsToAdd = calculatePointsToAdd(userMessageDto, messageLength);

        log.info("Finishing and saving the message and points information.");
        UserMessageEntity userMessage = new UserMessageEntity(user, messageLength, pointsToAdd, false);
        userMessageRepository.save(userMessage);
        user.setPointsToAdd(pointsToAdd);
        lastUserMessageService.updateUserLastMessage(lastUserMessage, message);
        userService.saveUser(user);
    }

    private BigDecimal calculatePointsToAdd(UserMessageDto userMessageDto, int messageLength) {
        log.info("Starting to calculate the percentage that user will receive with this message.q");

        int percentage = calculatePercentage(userMessageDto.isSubscriber(),
                userMessageDto.getSubscriptionTime(), userMessageDto.getSubscriptionTier());

        double pointsToAdd = (Double.parseDouble(String.valueOf(messageLength)) / percentage);

        log.info("Finished calculation.");
        return BigDecimal.valueOf(pointsToAdd).setScale(2, RoundingMode.HALF_EVEN);
    }

    private int calculatePercentage(boolean isSubscriber, int subscriptionTime, int subscriptionTier) {
        if (isSubscriber) {
            int subtractPercentage = subscriptionTime / 10;
            return SUBSCRIPTION_PERCENTAGES[subscriptionTier - 1] - subtractPercentage;
        }

        return PointsConstant.PERCENTAGE_FOR_NON_SUBS;
    }
}
