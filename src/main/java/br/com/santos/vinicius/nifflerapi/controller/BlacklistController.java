package br.com.santos.vinicius.nifflerapi.controller;

import br.com.santos.vinicius.nifflerapi.model.dto.BlacklistDto;
import br.com.santos.vinicius.nifflerapi.model.response.ErrorResponse;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.model.response.SuccessResponse;
import br.com.santos.vinicius.nifflerapi.service.BlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Blacklist", description = "Manage the blacklist to not allow users receive points")
@RestController
@RequestMapping("v2/blacklist")
public class BlacklistController {

    @Autowired
    BlacklistService blacklistService;

    @Operation(
            summary = "Retrieve a user in blacklist by username.",
            description = "Get a user that are in the blacklist by they username. Being in the blacklist they will not be able to receive points"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<Response> getUserInBlacklistByUsername(@PathVariable String username) {
        return blacklistService.getUserInBlacklistByUsername(username);
    }

    @Operation(
            summary = "Retrieve a user in blacklist by username.",
            description = "Get a user that are in the blacklist by they username. Being in the blacklist they will not be able to receive points"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @GetMapping("/id/{userId}")
    public ResponseEntity<Response> getUserInBlacklistByUserId(@PathVariable Long userId) {
        return blacklistService.getUserInBlacklistByUserId(userId);
    }

    @Operation(
            summary = "Retrieve all users in blacklist.",
            description = "Get all users in the blacklist. Being in the blacklist they will not be able to receive points"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @GetMapping("")
    public ResponseEntity<Response> getAllUsersInBlacklist() {
        return blacklistService.getAllUsersInBlacklist();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @PostMapping("")
    public ResponseEntity<Response> addUserInBlacklist(@RequestBody BlacklistDto blacklistDto) throws IOException {
        return blacklistService.addUserInBlacklist(blacklistDto);
    }

    @Operation(
            summary = "Delete a user from blacklist by username.",
            description = "Delete a user from blacklist by username. Not being in the blacklist they will be able to receive points"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @DeleteMapping("/username/{username}")
    public ResponseEntity<Response> deleteUserFromBlacklistByUsername(@PathVariable String username) {
        return blacklistService.removeUserFromBlacklistByUsername(username);
    }

    @Operation(
            summary = "Delete a user from blacklist by user id.",
            description = "Delete a user from blacklist by user id. Not being in the blacklist they will be able to receive points"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")})
    })
    @DeleteMapping("/id/{userId}")
    public ResponseEntity<Response> deleteUserFromBlacklistByUserId(@PathVariable Long userId) {
        return blacklistService.removeUserFromBlacklistByUserId(userId);
    }
}
