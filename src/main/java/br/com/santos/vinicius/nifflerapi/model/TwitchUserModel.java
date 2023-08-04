package br.com.santos.vinicius.nifflerapi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TwitchUserModel implements Serializable {

    private static final long serialVersionUID = 6537742026737313231L;

    private List<TwitchUserModelData> data = new ArrayList<>();
}

