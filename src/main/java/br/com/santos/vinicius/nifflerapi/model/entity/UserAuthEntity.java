package br.com.santos.vinicius.nifflerapi.model.entity;

import br.com.santos.vinicius.nifflerapi.constant.UserOriginEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_auth")
public class UserAuthEntity implements Serializable {

    private static final long serialVersionUID = 5607306447616438723L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    private String password;

    @Column
    private UserOriginEnum origin;

}
