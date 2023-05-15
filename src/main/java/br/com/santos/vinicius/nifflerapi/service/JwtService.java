package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.UserAuthModel;
import br.com.santos.vinicius.nifflerapi.model.entity.UserAuthEntity;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    UserAuthModel generateToken(UserAuthEntity userAuthEntity);

    boolean isTokenValid(String token);

    String getUserNameFromToken(String token);
}
