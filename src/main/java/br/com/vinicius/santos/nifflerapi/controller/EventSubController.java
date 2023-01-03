package br.com.vinicius.santos.nifflerapi.controller;

import br.com.vinicius.santos.nifflerapi.service.EventSubService;
import br.com.vinicius.santos.nifflerlib.model.dto.EventDto;
import br.com.vinicius.santos.nifflerlib.model.dto.EventSubDto;
import br.com.vinicius.santos.nifflerlib.model.dto.SubscriptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/event-sub")
public class EventSubController {

    @Autowired
    EventSubService eventSubService;

    @PostMapping("listen")
    public @ResponseBody ResponseEntity listenTwitchEventSub(@RequestBody EventSubDto eventSub) {
        System.out.println(eventSub);
        return eventSubService.checkEventSub(eventSub);
    }
}
