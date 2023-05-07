package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import org.springframework.stereotype.Service;

@Service
public interface LastUserMessageService {

    LastUserMessageEntity findUserLastMessage(Long userId);

    void updateUserLastMessage(LastUserMessageEntity lastUserMessage, String newMessage);

}
