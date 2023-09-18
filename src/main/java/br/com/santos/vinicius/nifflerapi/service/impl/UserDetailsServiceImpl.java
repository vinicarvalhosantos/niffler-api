package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
import br.com.santos.vinicius.nifflerapi.model.entity.UserAuthEntity;
import br.com.santos.vinicius.nifflerapi.service.UserAuthService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserAuthService userAuthService;

    public UserDetailsServiceImpl(UserAuthService userAuthService) {
        Assert.notNull(userAuthService, "UserAuthService must not be null");
        this.userAuthService = userAuthService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthEntity userAuth = userAuthService.findUserAuthByUsername(username);
        if (userAuth == null) {
            throw new NoSuchElementFoundException("An error occurred when tried to authenticate the user! User does not exists.");
        }

        return new User(userAuth.getUserName(), userAuth.getPassword(), new ArrayList<>());
    }
}
