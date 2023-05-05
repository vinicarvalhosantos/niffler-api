package br.com.santos.vinicius.nifflerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalServerException extends ResponseStatusException {

    private static final long serialVersionUID = 9104815480079146453L;

    public InternalServerException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }
}
