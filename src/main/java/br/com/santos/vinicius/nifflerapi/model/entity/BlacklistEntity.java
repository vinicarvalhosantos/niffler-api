package br.com.santos.vinicius.nifflerapi.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "blacklist")
@EntityListeners(AuditingEntityListener.class)
public class BlacklistEntity implements Serializable {

    private static final long serialVersionUID = -2652239657720907638L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String username;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @CreatedDate
    private Date createdAt;

    public BlacklistEntity(String username, UserEntity user) {
        this.user = user;
        this.createdAt = new Date();
        this.username = username;
    }

    public boolean equalsTwitchUser(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        return Objects.equals(this.username, ((UserEntity) obj).getUsername())
                && Objects.equals(this.user.getId(), ((UserEntity) obj).getId());
    }

}
