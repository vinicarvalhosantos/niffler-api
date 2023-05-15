package br.com.santos.vinicius.nifflerapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserAuthModel {

    private String accessToken;

    private Long expiresIn;


}
