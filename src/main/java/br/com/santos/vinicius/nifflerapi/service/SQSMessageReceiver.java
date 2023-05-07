package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;
import retrofit2.http.Header;

import java.io.IOException;

@Service
public interface SQSMessageReceiver {

    @SqsListener(value = "user_message_queue_dev.fifo", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    void receiveMessage(UserMessageDto message, @Header("SenderId") String senderId) throws IOException;

}
