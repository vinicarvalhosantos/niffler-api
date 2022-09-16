package br.com.vinicius.santos.nifflerapi.service;


public interface UserMessageService {

    void sendToQueueToAnalyseMessage(Object userMessageDto) throws Exception;

}
