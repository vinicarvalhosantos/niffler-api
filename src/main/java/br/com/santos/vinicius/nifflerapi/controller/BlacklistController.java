package br.com.santos.vinicius.nifflerapi.controller;

import br.com.santos.vinicius.nifflerapi.controller.handler.ExceptionsHandler;
import br.com.santos.vinicius.nifflerapi.model.dto.BlacklistDto;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.service.BlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Blacklist", description = "Manage the blacklist to not allow users receive points")
@RestController
@RequestMapping("v2/blacklist")
public class BlacklistController extends ExceptionsHandler {

    @Autowired
    BlacklistService blacklistService;


    @GetMapping("/username/{username}")
    public ResponseEntity<Response> getUserInBlacklistByUsername(@PathVariable String username) {
        return blacklistService.getUserInBlacklistByUsername(username);
    }


    @GetMapping("/id/{userId}")
    public ResponseEntity<Response> getUserInBlacklistByUserId(@PathVariable Long userId) {
        return blacklistService.getUserInBlacklistByUserId(userId);
    }

    @Operation(
            summary = "Retrieve all users in blacklist.",
            description = "Get all users in the blacklist. Being in the blacklist they will not be able to receive points"
    )

    @GetMapping("")
    public ResponseEntity<Response> getAllUsersInBlacklist() {
        return blacklistService.getAllUsersInBlacklist();
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
