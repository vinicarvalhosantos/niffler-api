package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.entity.LastUserMessageEntity;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LastUserMessageService {

    LastUserMessageEntity findUserLastMessage(UserEntity user);

    void updateUserLastMessage(LastUserMessageEntity lastUserMessage, String newMessage);

    void clearLastUserMessages();

    void deleteUserLastMessageByUsers(List<UserEntity> userEntityList);

}
