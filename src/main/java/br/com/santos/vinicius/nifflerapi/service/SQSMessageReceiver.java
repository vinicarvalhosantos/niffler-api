package br.com.santos.vinicius.nifflerapi.service;

import br.com.santos.vinicius.nifflerapi.model.dto.UserDeletedMessageDto;
import br.com.santos.vinicius.nifflerapi.model.dto.UserMessageDto;
import br.com.santos.vinicius.nifflerapi.model.dto.UserTimedOutDto;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;
import retrofit2.http.Header;

import java.io.IOException;

@Service
public interface SQSMessageReceiver {

    @SqsListener(value = "${aws.sqs.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    void receiveMessage(UserMessageDto message, @Header("SenderId") String senderId) throws IOException;

    @SqsListener(value = "${aws.sqs.message.deleted.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    void receiveDeletedMessage(UserDeletedMessageDto deletedMessageDto, @Header("SenderId") String senderId);

    @SqsListener(value = "${aws.sqs.timeout.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    void receiveUserTimedOut(UserTimedOutDto timedOutDto, String senderId);

}
