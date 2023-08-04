package br.com.santos.vinicius.nifflerapi.model.entity;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModelData;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -2091522841039993308L;

    @Id
    @Column(unique = true)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String displayName;

    @Column
    private BigDecimal pointsToAdd = BigDecimal.ZERO;

    @Column
    private BigDecimal pointsAdded = BigDecimal.ZERO;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    public UserEntity(Long id, String username, String displayName, BigDecimal pointsToAdd, BigDecimal pointsAdded) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.pointsToAdd = pointsToAdd;
        this.pointsAdded = pointsAdded;
    }

    public boolean equalsTwitchUser(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        return Objects.equals(this.username, ((TwitchUserModelData) obj).getLogin())
                && Objects.equals(this.displayName, ((TwitchUserModelData) obj).getDisplay_name());
    }

    public void fetchUserFromTwitchUser(Object obj) {
        if (this.equalsTwitchUser(obj)) return;

        this.username = ((TwitchUserModelData) obj).getLogin();
        this.displayName = ((TwitchUserModelData) obj).getDisplay_name();
    }

    public boolean equalsUserMessage(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        return Objects.equals(this.username, ((UserMessageDto) obj).getUsername())
                && Objects.equals(this.displayName, ((UserMessageDto) obj).getDisplayName());
    }

    public void fetchUserFromMessage(Object obj) {
        if (this.equalsUserMessage(obj)) return;

        this.username = ((UserMessageDto) obj).getUsername();
        this.displayName = ((UserMessageDto) obj).getDisplayName();
    }
}
