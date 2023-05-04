package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {

    ResponseEntity<Response> getAllUsers() throws IOException;

    ResponseEntity<Response> fetchAllUsers() throws IOException;

    UserEntity fetchUserByUsername(String username) throws IOException;

    TwitchUserModel getTwitchUser(String username) throws IOException;

    TwitchUserModel getTwitchUsersByIds(List<UserEntity> userEntityList) throws IOException;

}
