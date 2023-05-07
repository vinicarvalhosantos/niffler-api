package br.com.santos.vinicius.nifflerapi.model.dto;

import br.com.santos.vinicius.nifflerapi.util.EmoteUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
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

    private List<String> emotes;

    @Override
    public String toString() {
        return "UserMessageDto{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", userId=" + userId +
                ", subscriber=" + subscriber +
                ", subscriptionTime=" + subscriptionTime +
                ", emoteOnly=" + emoteOnly +
                ", emotes=" + emotes +
                '}';
    }

    public int messageLength() {
        if (this.emoteOnly) {
            return this.emotes.size();
        }
        List<String> emotes = EmoteUtil.extractWrittenEmotes(this.emotes, this.message);
        String messageWithoutEmotes = EmoteUtil.removeEmotesFromMessage(emotes, this.message);

        return messageWithoutEmotes.length() + emotes.size();
    }
}
