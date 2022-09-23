package br.com.vinicius.santos.nifflerapi.service;

import br.com.vinicius.santos.nifflerlib.model.dto.BlacklistDto;
import br.com.vinicius.santos.nifflerlib.model.entity.BlacklistEntity;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface BlacklistService {

    ResponseEntity<BlacklistEntity> addUserInBlacklist(BlacklistDto blacklistDto) throws IOException;

}
