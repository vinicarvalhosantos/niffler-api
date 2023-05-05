package br.com.santos.vinicius.nifflerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchElementFoundException extends ResponseStatusException {


    private static final long serialVersionUID = -6571049353319382174L;

    public NoSuchElementFoundException(HttpStatus status) {
        super(status);
    }

    public NoSuchElementFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public NoSuchElementFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public NoSuchElementFoundException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
