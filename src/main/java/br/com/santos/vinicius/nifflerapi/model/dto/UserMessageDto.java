package br.com.santos.vinicius.nifflerapi.model.dto;

import br.com.santos.vinicius.nifflerapi.util.EmoteUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserMessageDto implements Serializable {

    private static final long serialVersionUID = -969841145487728630L;

    private String message;

    private String username;

    private String displayName;

    private Long userId;

    private boolean subscriber;

    private int subscriptionTime;

    private int subscriptionTier = 0;

    private boolean emoteOnly;

    private List<String> emotesSent;

    public int messageLength() {
        if (this.emoteOnly) {
            return this.emotesSent.size();
        }
        List<String> emotes = EmoteUtil.extractWrittenEmotes(this.emotesSent, this.message);
        String messageWithoutEmotes = EmoteUtil.removeEmotesFromMessage(emotes, this.message);

        return messageWithoutEmotes.length() + emotes.size();
    }
}
