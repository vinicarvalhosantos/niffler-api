package br.com.santos.vinicius.nifflerapi.controller;

import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
import br.com.santos.vinicius.nifflerapi.model.response.ErrorResponse;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "User", description = "Manage the users from twitch")
@RestController
@RequestMapping("v2/user")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("")
    public ResponseEntity<Response> getAllUsers() throws IOException {
        return userService.getAllUsers();
    }


    @PutMapping("/fetch")
    public ResponseEntity<Response> fetchUsers() throws IOException, InterruptedException {
        return userService.fetchAllUsers();
    }

    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response> handleNoSuchElementFound(NoSuchElementFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name());

        return ResponseEntity.status(exception.getStatus()).body(new Response(errorResponse));
    }

}
