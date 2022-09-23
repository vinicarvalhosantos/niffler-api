package br.com.vinicius.santos.nifflerapi.controller;


import br.com.vinicius.santos.nifflerapi.service.BlacklistService;
import br.com.vinicius.santos.nifflerlib.model.dto.BlacklistDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/v1/blacklist")
public class BlacklistController {

    @Autowired
    BlacklistService blacklistService;

    @PostMapping("add")
    public @ResponseBody ResponseEntity addUserInBlacklist(@RequestBody BlacklistDto blacklistDto) {
        try {
            return blacklistService.addUserInBlacklist(blacklistDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
