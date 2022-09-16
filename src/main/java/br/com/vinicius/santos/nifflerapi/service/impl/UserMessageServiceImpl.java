package br.com.vinicius.santos.nifflerapi.service.impl;

import br.com.vinicius.santos.nifflerapi.service.UserMessageService;
import br.com.vinicius.santos.nifflerlib.constants.RabbitMqConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void sendToQueueToAnalyseMessage(Object userMessageDto) {
        String queueName = RabbitMqConstants.MESSAGES_BOT_QUEUE;
        this.rabbitTemplate.convertAndSend(queueName, userMessageDto);
    }
}
