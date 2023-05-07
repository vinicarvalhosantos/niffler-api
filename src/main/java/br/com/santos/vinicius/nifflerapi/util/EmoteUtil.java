package br.com.santos.vinicius.nifflerapi.util;

import java.util.ArrayList;
import java.util.List;

public class EmoteUtil {

    private EmoteUtil(){

    }

    public static List<String> extractWrittenEmotes(List<String> emotes, String message) {
        List<String> writtenEmotes = new ArrayList<>();

        for (String emote : emotes) {

            String[] allEmoteHash = emote.split(":");

            String[] emoteAllPositionsMessage = allEmoteHash[1].split(",");

            String[] emotePositionMessage = emoteAllPositionsMessage[0].split("-");

            int emoteFirstPosition = Integer.parseInt(emotePositionMessage[0]);

            int emoteSecondPosition = Integer.parseInt(emotePositionMessage[1]) + 1;

            String emoteText = message.substring(emoteFirstPosition, emoteSecondPosition);
            writtenEmotes.add(emoteText);

        }

        return writtenEmotes;
    }

    public static String removeEmotesFromMessage(List<String> emotes, String message) {
        final String[] messageFormatted = {message};
        emotes.forEach(writtenEmoji -> messageFormatted[0] = messageFormatted[0].replaceAll(writtenEmoji, ""));

        return messageFormatted[0].trim();
    }

}
