package br.com.santos.vinicius.nifflerapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserTimedOutDto implements Serializable {

    private static final long serialVersionUID = 4738639076127608481L;

    private Long userId;

}
