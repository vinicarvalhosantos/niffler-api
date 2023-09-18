package br.com.santos.vinicius.nifflerapi.controller.v2;

import br.com.santos.vinicius.nifflerapi.controller.handler.ExceptionsHandler;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("v2/user")
public class UserController extends ExceptionsHandler {

    final UserService userService;

    public UserController(UserService userService) {
        Assert.notNull(userService, "UserService must not be null");
        this.userService = userService;
    }


    @GetMapping("")
    public ResponseEntity<Response> getAllUsers(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "50") int limit) {
        return userService.getAllUsers(page, limit);
    }


    @PutMapping("/fetch")
    public ResponseEntity<Response> fetchUsers() throws IOException, InterruptedException {
        return userService.fetchAllUsers();
    }

}
