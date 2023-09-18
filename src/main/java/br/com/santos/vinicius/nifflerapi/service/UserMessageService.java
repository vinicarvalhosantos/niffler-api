package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface UserMessageService {

    void messageAnalysis(UserMessageDto userMessageDto) throws IOException;

    void deleteUserMessage(String messageId);

    void deleteUserLastMessage(Long userId);

    void clearOldMessagePoints();

    void updateDeletedUserMessage(List<UserEntity> userEntityList, UserEntity userDeleted);
}
