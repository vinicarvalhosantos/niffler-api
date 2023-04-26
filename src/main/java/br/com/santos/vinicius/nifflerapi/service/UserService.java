package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserService {

    UserEntity findUserByUserId(Long id);

    UserEntity findUserByUsername(String username);

    UserEntity fetchUserByUsername(String username) throws IOException;

    TwitchUserModel getTwitchUser(String username) throws IOException;

}
