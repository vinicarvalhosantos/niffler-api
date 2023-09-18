package br.com.santos.vinicius.nifflerapi.event.impl;

import br.com.santos.vinicius.nifflerapi.event.ApplicationContextRefreshedEvent;
import br.com.santos.vinicius.nifflerapi.service.LastUserMessageService;
import br.com.santos.vinicius.nifflerapi.service.UserMessageService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationContextRefreshedEventImpl implements ApplicationContextRefreshedEvent {

    final LastUserMessageService lastUserMessageService;

    final UserMessageService userMessageService;

    public ApplicationContextRefreshedEventImpl(LastUserMessageService lastUserMessageService, UserMessageService userMessageService) {
        Assert.notNull(lastUserMessageService, "LastUserMessageService must not be null");
        Assert.notNull(userMessageService, "UserMessageService must not be null");
        this.lastUserMessageService = lastUserMessageService;
        this.userMessageService = userMessageService;
    }

    @Override
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefresh() {
        lastUserMessageService.clearLastUserMessages();
        //userMessageService.clearOldMessagePoints();
    }
}
