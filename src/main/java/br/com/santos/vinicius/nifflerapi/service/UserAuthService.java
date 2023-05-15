package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.dto.AuthenticateDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserAuthEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserAuthService {

    ResponseEntity<Response> register(UserAuthEntity userAuthEntity);

    ResponseEntity<Response> authenticate(AuthenticateDto authenticateDto);

    UserAuthEntity findUserAuthByUsername(String username);

}
