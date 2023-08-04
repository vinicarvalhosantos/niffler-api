package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.exception.ElementAlreadyReportedException;
import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
import br.com.santos.vinicius.nifflerapi.model.UserAuthModel;
import br.com.santos.vinicius.nifflerapi.model.dto.AuthenticateDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserAuthEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.model.response.SuccessResponse;
import br.com.santos.vinicius.nifflerapi.repository.UserAuthRepository;
import br.com.santos.vinicius.nifflerapi.service.EncryptService;
import br.com.santos.vinicius.nifflerapi.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private JwtServiceImpl jwtServiceImpl;

    @Override
    public ResponseEntity<Response> register(UserAuthEntity userAuthEntity) {
        Optional<UserAuthEntity> userAuth = userAuthRepository.findByUserName(userAuthEntity.getUserName());

        if (userAuth.isPresent()) {
            throw new ElementAlreadyReportedException("User already registered.");
        }

        userAuthEntity.setPassword(encryptService.encrypt(userAuthEntity.getPassword()));

        SuccessResponse successResponse = new SuccessResponse(List.of(userAuthRepository.save(userAuthEntity)),
                "User created with successfull.");

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @Override
    public ResponseEntity<Response> authenticate(AuthenticateDto authenticateDto) {
        Optional<UserAuthEntity> userAuthEntity = userAuthRepository.findByUserName(authenticateDto.getUserName());
        String password = authenticateDto.getPassword();

        if (userAuthEntity.isEmpty()) {
            throw new NoSuchElementFoundException("User name or password is wrong!");
        }

        UserAuthEntity userAuth = userAuthEntity.get();

        if (!encryptService.matches(password, userAuth.getPassword())) {
            throw new NoSuchElementFoundException("User name or password is wrong!");
        }
        UserAuthModel userAuthModel = jwtServiceImpl.generateToken(userAuth);

        SuccessResponse successResponse = new SuccessResponse(List.of(userAuthModel), "User successfully authenticated.");

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @Override
    public UserAuthEntity findUserAuthByUsername(String username) {
        Optional<UserAuthEntity> userAuthEntity = userAuthRepository.findByUserName(username);

        return userAuthEntity.orElse(null);
    }
}
