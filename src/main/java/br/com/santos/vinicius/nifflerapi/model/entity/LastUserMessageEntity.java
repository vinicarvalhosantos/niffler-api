package br.com.santos.vinicius.nifflerapi.model.entity;

import br.com.santos.vinicius.nifflerapi.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "last_user_message")
public class LastUserMessageEntity implements Serializable {

    private static final long serialVersionUID = 689045718584315639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @Column
    private String lastMessage;

    public LastUserMessageEntity(UserEntity user, String lastMessage) {
        this.user = user;
        this.lastMessage = lastMessage;
    }

    public double compareMessages(String recentMessage) {
        if (recentMessage.length() > this.lastMessage.length()) {
            return StringUtil.similarity(recentMessage, this.lastMessage);
        }

        return StringUtil.similarity(this.lastMessage, recentMessage);
    }
}
