package br.com.santos.vinicius.nifflerapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDeletedMessageDto implements Serializable {

    private static final long serialVersionUID = -5196374489798359356L;

    private String messageId;
}
