package br.com.santos.vinicius.nifflerapi.model;


import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserFetchModel implements Serializable {

    private static final long serialVersionUID = 85722416837711885L;

    private TwitchUserModel twitchUsers;

    private List<UserEntity> userEntityList;

    private List<Thread> threadList;
}
