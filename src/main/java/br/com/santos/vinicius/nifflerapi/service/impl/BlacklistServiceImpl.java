package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.dto.BlacklistDto;
import br.com.santos.vinicius.nifflerapi.model.entity.BlacklistEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import br.com.santos.vinicius.nifflerapi.model.response.ErrorResponse;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.model.response.SuccessResponse;
import br.com.santos.vinicius.nifflerapi.repository.BlacklistRepository;
import br.com.santos.vinicius.nifflerapi.service.BlacklistService;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    BlacklistRepository blacklistRepository;

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<Response> addUserInBlacklist(BlacklistDto blacklistDto) throws IOException {
        String username = blacklistDto.getUsername();

        UserEntity user = userService.fetchUserByUsername(username);

        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse("This user does not a valid user", 404, HttpStatus.NOT_FOUND.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(errorResponse));
        }

        Optional<BlacklistEntity> blacklistEntityOptional = blacklistRepository.findByUserId(user.getUserId());

        if (blacklistEntityOptional.isEmpty()) {
            BlacklistEntity blacklistEntity = new BlacklistEntity(blacklistDto.getUsername(), user.getUserId());
            blacklistEntity = blacklistRepository.save(blacklistEntity);
            SuccessResponse successResponse = new SuccessResponse(List.of(blacklistEntity), "User added in blacklist.");

            return ResponseEntity.status(HttpStatus.CREATED).body(new Response(successResponse));
        }

        BlacklistEntity blacklistEntity = blacklistEntityOptional.get();
        if (!blacklistEntity.equalsTwitchUser(user)) {
            blacklistEntity.setUsername(user.getUsername());
            blacklistRepository.save(blacklistEntity);
            SuccessResponse successResponse = new SuccessResponse(List.of(blacklistEntity), "User updated in blacklist.");

            return ResponseEntity.status(HttpStatus.CREATED).body(new Response(successResponse));
        }

        ErrorResponse errorResponse = new ErrorResponse("This user already exists in the blacklist", 208, "ALREADY_REPORTED");

        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new Response(errorResponse));
    }

    @Override
    public ResponseEntity<Response> getUserInBlacklistByUsername(String username) {
        Optional<BlacklistEntity> blacklistEntity = blacklistRepository.findByUsername(username);

        return getUserInBlacklist(blacklistEntity);
    }

    @Override
    public ResponseEntity<Response> getUserInBlacklistByUserId(Long userId) {
        Optional<BlacklistEntity> blacklistEntity = blacklistRepository.findByUserId(userId);

        return getUserInBlacklist(blacklistEntity);
    }

    @Override
    public ResponseEntity<Response> getAllUsersInBlacklist() {
        List<BlacklistEntity> blacklistEntityList = IteratorUtils.toList(blacklistRepository.findAll().iterator());

        if (blacklistEntityList.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Any user are in blacklist", 404, HttpStatus.NOT_FOUND.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(errorResponse));
        }

        SuccessResponse successResponse = new SuccessResponse(formatRecords(blacklistEntityList), "Users were found in blacklist");

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));
    }

    @Override
    public ResponseEntity<Response> removeUserFromBlacklistByUsername(String username) {
        blacklistRepository.deleteByUsername(username);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    public ResponseEntity<Response> removeUserFromBlacklistByUserId(Long userId) {
        blacklistRepository.deleteByUserId(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    private ResponseEntity<Response> getUserInBlacklist(Optional<BlacklistEntity> blacklistEntity) {

        if (blacklistEntity.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("User was not found in blacklist", 404, HttpStatus.NOT_FOUND.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(errorResponse));
        }
        List<Object> records = List.of(blacklistEntity.get());

        SuccessResponse successResponse = new SuccessResponse(records, "User found in blacklist");

        return ResponseEntity.status(HttpStatus.OK).body(new Response(successResponse));

    }

    private List<Object> formatRecords(List<BlacklistEntity> blacklistEntityList) {
        //Make the list of list to just one list
        return Stream.of(blacklistEntityList).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
