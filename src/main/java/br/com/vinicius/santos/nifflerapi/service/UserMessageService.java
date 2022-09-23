package br.com.vinicius.santos.nifflerapi.service;


import br.com.vinicius.santos.nifflerlib.model.dto.UserMessageDto;

public interface UserMessageService {

    void sendToQueueToAnalyseMessage(UserMessageDto userMessageDto) throws Exception;

}
