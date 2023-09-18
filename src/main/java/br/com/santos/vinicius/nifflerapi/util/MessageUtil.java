package br.com.santos.vinicius.nifflerapi.util;

import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * In development
 */
@Component
public class MessageUtil {

    static UserService userService = null;

    private static final String[] greetings = {"bom dia", "bodia", "bodos", "boa tarde", "boa tardinha", "batarde", "boa noite", "boa noitinha", "banoite", "bom descanso", "boom descansoo", "bom descansoo"};

    public MessageUtil(UserService userService) {
        MessageUtil.userService = userService;
    }

    public static boolean isMessagePolite(String message) {
        if (containsGreetings(message)) {
            return true;
        }

        return containsGreetingsPercentage(message);
    }

    private static boolean containsGreetings(String message) {

        return StringUtils.containsAnyIgnoreCase(message, greetings);
    }

    private static boolean containsGreetingsPercentage(String message) {
        String[] messageSplitted = message.split(" ");
        boolean isPolite = false;
        for (int i = 0; i < messageSplitted.length; i++) {
            if (isPercentagePolite(messageSplitted[i])) {
                isPolite = true;
                break;
            }

            if (i == (messageSplitted.length - 1)) {
                break;
            }
            String messageToCheck = messageSplitted[i] + " " + messageSplitted[i + 1];

            if (isPercentagePolite(messageToCheck)) {
                isPolite = true;
                break;
            }
        }

        return isPolite;
    }

    private static boolean isPercentagePolite(String messageToCheck) {
        boolean isPolite = false;

        for (String greeting : greetings) {
            String shorter;
            String longer;

            if (messageToCheck.length() > greeting.length()) {
                shorter = greeting.toLowerCase();
                longer = messageToCheck.toLowerCase();
            } else {
                shorter = messageToCheck.toLowerCase();
                longer = greeting.toLowerCase();
            }

            if (StringUtil.similarity(longer, shorter) >= 0.70) {
                isPolite = true;
                break;
            }
        }

        return isPolite;
    }

    public static List<UserEntity> findUsersMarked(String message) {
        List<String> messageSplitted = List.of(message.split(" "));
        messageSplitted = messageSplitted.stream().map(element -> element.replace("@", "")).collect(Collectors.toList());

        return userService.findAllUsersMarked(messageSplitted);
    }

    public static String removeUsersMarkedFromMessage(String message, List<UserEntity> usersMarked) {
        List<String> usernamesMarked = usersMarked.stream().map(UserEntity::getUsername).collect(Collectors.toList());
        String messageFormatted = removeAtFromMessage(message);

        return StringUtil.replaceListElementsInString(messageFormatted, usernamesMarked);
    }

    public static String removeCheerFromMessage(String message) {
        String[] messageSplitted = message.split(" ");
        for (String messageElement : messageSplitted) {
            if (messageElement.startsWith("Cheer")) {
                message = message.replaceFirst(messageElement, "");
            }

        }
        return message.trim();
    }

    private static String removeAtFromMessage(String message) {
        StringBuilder messageFormatted = new StringBuilder();
        String[] messageSplitted = message.split(" ");

        for (int i = 0; i < messageSplitted.length; i++) {
            if (messageSplitted[i].startsWith("@")) {
                messageSplitted[i] = messageSplitted[i].replace("@", "");
            }

            messageFormatted.append(messageSplitted[i]);
        }

        return messageFormatted.toString();

    }

}
