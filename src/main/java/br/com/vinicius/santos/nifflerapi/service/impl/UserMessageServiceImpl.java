package br.com.vinicius.santos.nifflerapi.service.impl;

import br.com.vinicius.santos.nifflerapi.service.UserMessageService;
import br.com.vinicius.santos.nifflerlib.constants.RabbitMqConstants;
import br.com.vinicius.santos.nifflerlib.model.dto.UserMessageDto;
import br.com.vinicius.santos.nifflerlib.model.entity.BlacklistEntity;
import br.com.vinicius.santos.nifflerlib.repository.BlacklistRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BlacklistRepository blacklistRepository;


    @Override
    public void sendToQueueToAnalyseMessage(UserMessageDto userMessageDto) {

        Optional<BlacklistEntity> optionalBlacklistEntity = this.blacklistRepository.findBlacklistEntityByUser_Id(userMessageDto.getUserId());

        if(optionalBlacklistEntity.isPresent()){
            return;
        }

        String queueName = RabbitMqConstants.MESSAGES_BOT_QUEUE;
        this.rabbitTemplate.convertAndSend(queueName, userMessageDto);
    }
}
