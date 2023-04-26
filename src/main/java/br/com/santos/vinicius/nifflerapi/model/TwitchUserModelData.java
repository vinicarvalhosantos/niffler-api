package br.com.santos.vinicius.nifflerapi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TwitchUserModelData implements Serializable {

    private static final long serialVersionUID = -1105795668989927657L;

    private String id;

    private String login;

    private String display_name;
}
