package br.com.vinicius.santos.nifflerapi.controller;

import br.com.vinicius.santos.nifflerapi.service.UserMessageService;
import br.com.vinicius.santos.nifflerlib.models.dto.UserMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/user-message")
public class UserMessageController {

    @Autowired
    UserMessageService userMessageService;

    @PostMapping("analyse")
    public @ResponseBody ResponseEntity analyseMessage(@RequestBody UserMessageDto userMessageDto) {
        try {
            this.userMessageService.sendToQueueToAnalyseMessage(userMessageDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

}
