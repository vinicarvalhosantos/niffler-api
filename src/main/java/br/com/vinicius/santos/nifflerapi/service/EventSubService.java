package br.com.vinicius.santos.nifflerapi.service;

import br.com.vinicius.santos.nifflerlib.model.dto.EventSubDto;
import org.springframework.http.ResponseEntity;

public interface EventSubService {

    ResponseEntity<Object> checkEventSub(EventSubDto eventSub);

}
