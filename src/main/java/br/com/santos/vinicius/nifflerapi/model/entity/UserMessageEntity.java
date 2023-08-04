package br.com.santos.vinicius.nifflerapi.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "message_points")
@EntityListeners(AuditingEntityListener.class)
public class UserMessageEntity implements Serializable {

    private static final long serialVersionUID = 2587708892697687317L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private int messageLength;

    @Column(nullable = false)
    private BigDecimal pointsToAdd;

    @Column(name = "is_spam", nullable = false)
    private boolean spam;

    @Column(nullable = false)
    private String messageId;

    @Column(nullable = false)
    private boolean deleted;

    @CreatedDate
    private Date createdAt;

    public UserMessageEntity(UserEntity user, int messageLength, BigDecimal pointsToAdd, boolean spam, String messageId) {
        this.user = user;
        this.messageLength = messageLength;
        this.pointsToAdd = pointsToAdd;
        this.spam = spam;
        this.messageId = messageId;
        this.deleted = false;
    }

    public UserMessageEntity(Long id, UserEntity user, int messageLength, BigDecimal pointsToAdd, boolean spam, String messageId, boolean deleted, Date createdAt) {
        this.id = id;
        this.user = user;
        this.messageLength = messageLength;
        this.pointsToAdd = pointsToAdd;
        this.spam = spam;
        this.messageId = messageId;
        this.deleted = deleted;
        this.createdAt = createdAt;
    }
}
