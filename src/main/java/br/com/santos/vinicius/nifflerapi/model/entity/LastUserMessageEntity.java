package br.com.santos.vinicius.nifflerapi.model.entity;

import br.com.santos.vinicius.nifflerapi.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "last_user_message")
@EntityListeners(AuditingEntityListener.class)
public class LastUserMessageEntity implements Serializable {

    private static final long serialVersionUID = 689045718584315639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(length = 500)
    private String lastMessage;

    @Column
    private LocalDate lastMessageAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public LastUserMessageEntity(UserEntity user, String lastMessage) {
        this.user = user;
        this.lastMessage = lastMessage;
        this.lastMessageAt = LocalDate.now();
    }

    public double compareMessages(String recentMessage) {
        if (recentMessage.length() > this.lastMessage.length()) {
            return StringUtil.similarity(recentMessage, this.lastMessage);
        }

        return StringUtil.similarity(this.lastMessage, recentMessage);
    }
}
