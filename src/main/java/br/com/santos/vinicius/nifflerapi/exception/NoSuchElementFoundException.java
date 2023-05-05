package br.com.santos.vinicius.nifflerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchElementFoundException extends ResponseStatusException {


    private static final long serialVersionUID = -6571049353319382174L;

    public NoSuchElementFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

}
