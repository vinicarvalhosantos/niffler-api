package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.TwitchUserModel;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    ResponseEntity<Response> getAllUsers();

    ResponseEntity<Response> fetchAllUsers() throws IOException, InterruptedException;

    UserEntity fetchUserByUsername(String username) throws IOException;

    UserEntity fetchFromUserMessage(UserMessageDto userMessageDto);

    TwitchUserModel getTwitchUser(String username) throws IOException;

    TwitchUserModel getTwitchUsersByIds(List<UserEntity> userEntityList) throws IOException;

    Optional<UserEntity> findUserById(Long id);

    void saveUser(UserEntity user);

}
