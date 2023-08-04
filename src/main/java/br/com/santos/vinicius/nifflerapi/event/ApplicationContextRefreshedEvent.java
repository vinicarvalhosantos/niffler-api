package br.com.santos.vinicius.nifflerapi.event;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public interface ApplicationContextRefreshedEvent {

    @EventListener(ContextRefreshedEvent.class)
    void onContextRefresh();

}
