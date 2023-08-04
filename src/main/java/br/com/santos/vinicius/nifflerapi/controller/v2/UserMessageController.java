package br.com.santos.vinicius.nifflerapi.controller.v2;

import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserMessageEntity;
import br.com.santos.vinicius.nifflerapi.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("v2/message")
public class UserMessageController {

    @Autowired
    UserMessageService userMessageService;

    @PostMapping("/analyse")
    public ResponseEntity analyseMessage(@RequestBody UserMessageDto userMessageDto) throws IOException {
        userMessageService.messageAnalysis(userMessageDto);
        return null;
    }

}
