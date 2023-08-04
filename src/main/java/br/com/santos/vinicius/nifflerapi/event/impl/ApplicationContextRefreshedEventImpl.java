package br.com.santos.vinicius.nifflerapi.event.impl;

import br.com.santos.vinicius.nifflerapi.event.ApplicationContextRefreshedEvent;
import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationContextRefreshedEventImpl implements ApplicationContextRefreshedEvent {

    @Autowired
    LastUserMessageService lastUserMessageService;

    @Autowired
    UserMessageService userMessageService;

    @Override
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefresh() {
        lastUserMessageService.clearLastUserMessages();
        userMessageService.clearOldMessagePoints();
    }
}
