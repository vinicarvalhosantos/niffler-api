package br.com.santos.vinicius.nifflerapi.controller.v2;

import br.com.santos.vinicius.nifflerapi.controller.handler.ExceptionsHandler;
import br.com.santos.vinicius.nifflerapi.model.dto.BlacklistDto;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.service.BlacklistService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("v2/blacklist")
public class BlacklistController extends ExceptionsHandler {

    final BlacklistService blacklistService;

    public BlacklistController(BlacklistService blacklistService) {
        Assert.notNull(blacklistService, "BlacklistService must not be null");
        this.blacklistService = blacklistService;
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<Response> getUserInBlacklistByUsername(@PathVariable String username) {
        return blacklistService.getUserInBlacklistByUsername(username);
    }


    @GetMapping("/id/{userId}")
    public ResponseEntity<Response> getUserInBlacklistByUserId(@PathVariable Long userId) {
        return blacklistService.getUserInBlacklistByUserId(userId);
    }

    @GetMapping("")
    public ResponseEntity<Response> getAllUsersInBlacklist(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "50") int limit) {
        return blacklistService.getAllUsersInBlacklist(page, limit);
    }


    @PostMapping("")
    public ResponseEntity<Response> addUserInBlacklist(@RequestBody BlacklistDto blacklistDto) throws IOException {
        return blacklistService.addUserInBlacklist(blacklistDto);
    }


    @DeleteMapping("/username/{username}")
    public ResponseEntity<Response> deleteUserFromBlacklistByUsername(@PathVariable String username) {
        return blacklistService.removeUserFromBlacklistByUsername(username);
    }


    @DeleteMapping("/id/{userId}")
    public ResponseEntity<Response> deleteUserFromBlacklistByUserId(@PathVariable Long userId) {
        return blacklistService.removeUserFromBlacklistByUserId(userId);
    }
}
