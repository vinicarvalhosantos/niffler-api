package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.dto.BlacklistDto;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface BlacklistService {

    ResponseEntity<Response> addUserInBlacklist(BlacklistDto blacklistDto) throws IOException;

    ResponseEntity<Response> getUserInBlacklistByUsername(String username);

    ResponseEntity<Response> getUserInBlacklistByUserId(Long userId);

    ResponseEntity<Response> getAllUsersInBlacklist();

    ResponseEntity<Response> removeUserFromBlacklistByUsername(String username);

    ResponseEntity<Response> removeUserFromBlacklistByUserId(Long userId);

    boolean isUserInBlacklist(Long userId);
}
