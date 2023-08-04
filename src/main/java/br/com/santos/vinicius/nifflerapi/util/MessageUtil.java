package br.com.santos.vinicius.nifflerapi.util;

import org.apache.commons.lang3.StringUtils;

/**
 * In development
 */
public class MessageUtil {

    private static final String[] greetings = {"bom dia", "bodia", "bodos", "boa tarde", "boa tardinha", "batarde", "boa noite", "boa noitinha", "banoite"};

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

}
