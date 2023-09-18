package br.com.santos.vinicius.nifflerapi.controller.v2;

import br.com.santos.vinicius.nifflerapi.controller.handler.ExceptionsHandler;
import br.com.santos.vinicius.nifflerapi.model.dto.AuthenticateDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserAuthEntity;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import br.com.santos.vinicius.nifflerapi.service.UserAuthService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v2/user-auth")
public class UserAuthController extends ExceptionsHandler {

    final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        Assert.notNull(userAuthService, "UserAuthService must not be null");
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody UserAuthEntity userAuth) {
        return userAuthService.register(userAuth);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticateDto authenticateDto) {
        return userAuthService.authenticate(authenticateDto);
    }

}
