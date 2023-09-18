package br.com.santos.vinicius.nifflerapi.util;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class EmoteUtil {

    private EmoteUtil() {

    }

    public static List<String> extractWrittenEmotes(List<String> emotes, String message) {
        List<String> writtenEmotes = new ArrayList<>();

        for (String emote : emotes) {

            String[] allEmoteHash = emote.split(":");

            String[] emoteAllPositionsMessage = allEmoteHash[1].split(",");

            String[] emotePositionMessage = emoteAllPositionsMessage[0].split("-");

            int emoteFirstPosition = Integer.parseInt(emotePositionMessage[0]);

            int emoteSecondPosition = Integer.parseInt(emotePositionMessage[1]) + 1;

            if (emoteFirstPosition >= 0 && emoteSecondPosition <= message.length()) {
                String emoteText = message.substring(emoteFirstPosition, emoteSecondPosition);
                writtenEmotes.add(emoteText);

            } else {
                log.error("Was not possible to extract a specific emote. {}", emote);
            }

        }

        return writtenEmotes;
    }

    public static String removeEmotesFromMessage(List<String> emotes, String message) {
        return StringUtil.replaceListElementsInString(message, emotes);
    }

    public static int calculateEmotesNumberFromMessage(List<String> emotes) {
        if (emotes.isEmpty()) {
            return 0;
        }
        int emotesNumber = 0;

        for (String emote : emotes) {
            String[] allEmoteHash = emote.split(":");
            String[] emoteAllPositionsMessage = allEmoteHash[1].split(",");

            emotesNumber += emoteAllPositionsMessage.length;

        }

        return emotesNumber;
    }

}
