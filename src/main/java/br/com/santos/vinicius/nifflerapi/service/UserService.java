package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.dto.UserDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserService {

    ResponseEntity<Response> createUser(UserDto userDto) throws IOException;


    ResponseEntity<Response> getAllUsers() throws IOException;

    UserEntity findUserByUserId(Long id);

    UserEntity findUserByUsername(String username);

    UserEntity fetchUserByUsername(String username) throws IOException;

    UserEntity fetchUserByUserId(Long userId) throws IOException;

    TwitchUserModel getTwitchUser(String username) throws IOException;

    TwitchUserModel getTwitchUser(Long userId) throws IOException;

}
