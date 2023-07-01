package br.com.santos.vinicius.nifflerapi.model.entity;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModelData;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
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
    @ColumnDefault(value = "0.0")
    private BigDecimal pointsToAdd;

    @Column
    @ColumnDefault(value = "0.0")
    private BigDecimal pointsAdded;

    @CreatedDate
    private Date createdAt;

    public UserEntity(Long id, String username, String displayName, BigDecimal pointsToAdd, BigDecimal pointsAdded) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.pointsToAdd = pointsToAdd;
        this.pointsAdded = pointsAdded;
        this.createdAt = new Date();
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
