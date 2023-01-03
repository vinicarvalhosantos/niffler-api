package br.com.vinicius.santos.nifflerapi.service.impl;

import br.com.vinicius.santos.nifflerapi.service.EventSubService;
import br.com.vinicius.santos.nifflerlib.model.dto.EventDto;
import br.com.vinicius.santos.nifflerlib.model.dto.EventSubDto;
import br.com.vinicius.santos.nifflerlib.model.dto.SubscriptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EventSubServiceImpl implements EventSubService {
    @Override
    public ResponseEntity<Object> checkEventSub(EventSubDto eventSub) {
        SubscriptionDto subscription = eventSub.getSubscription();
        EventDto event = eventSub.getEvent();
        String eventType = subscription.getType();

        switch (eventType) {
            case "stream.online":
                streamGotOffline();
                break;
            case "stream.offline":
                streamGotOnline();
                break;
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

    private void streamGotOffline() {

    }

    private void streamGotOnline() {

    }
}
