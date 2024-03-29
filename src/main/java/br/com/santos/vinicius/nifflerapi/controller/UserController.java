package br.com.santos.vinicius.nifflerapi.controller;

import br.com.santos.vinicius.nifflerapi.controller.handler.ExceptionsHandler;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("v2/user")
public class UserController extends ExceptionsHandler {

    @Autowired
    UserService userService;


    @GetMapping("")
    public ResponseEntity<Response> getAllUsers() {
        return userService.getAllUsers();
    }


    @PutMapping("/fetch")
    public ResponseEntity<Response> fetchUsers() throws IOException, InterruptedException {
        return userService.fetchAllUsers();
    }

}
