package br.com.santos.vinicius.nifflerapi.model.dto;

import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.util.EmoteUtil;
import br.com.santos.vinicius.nifflerapi.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.regex.PatternSyntaxException;

@Getter
@Setter
@Slf4j
public class UserMessageDto implements Serializable {

    private static final long serialVersionUID = -969841145487728630L;

    private String message;

    private String username;

    private String displayName;

    private Long userId;

    private boolean subscriber;

    private boolean firstMessage;

    private int subscriptionTime;

    private int subscriptionTier = 0;

    private boolean emoteOnly;

    private List<String> emotesSent;

    private String messageId;

    private int cheers;

    public int messageLength() {
        try {
            if (this.emoteOnly) {
                return this.emotesSent.size();
            }
            List<String> emotes = EmoteUtil.extractWrittenEmotes(this.emotesSent, this.message);
            String messageWithoutEmotes = EmoteUtil.removeEmotesFromMessage(emotes, this.message);
            int emotesNumber = EmoteUtil.calculateEmotesNumberFromMessage(this.emotesSent);
            log.info("Checking if there are marked users in message.");
            List<UserEntity> usersMarked = MessageUtil.findUsersMarked(this.message);
            String messageWithoutEmotesAndUsersMarked = MessageUtil.removeUsersMarkedFromMessage(messageWithoutEmotes, usersMarked);
            String messageWithoutEmotesUsersMarkedAndCheers = MessageUtil.removeCheerFromMessage(messageWithoutEmotesAndUsersMarked);

            return messageWithoutEmotesUsersMarkedAndCheers.length() + emotesNumber + usersMarked.size();
        } catch (PatternSyntaxException ex) {
            log.error("Was not possible to calculate message length! {}", ex.getMessage());
            return 0;
        }
    }

    public boolean isMessagePolite() {
        return MessageUtil.isMessagePolite(this.message);
    }
}
