package br.com.santos.vinicius.nifflerapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TwitchTokenModel implements Serializable {

    private static final long serialVersionUID = -221258559163507349L;

    private String access_token;

    private Long expires_in;

    private String token_type;
}
