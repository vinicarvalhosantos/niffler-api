package br.com.santos.vinicius.nifflerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ElementAlreadyReportedException extends ResponseStatusException {


    private static final long serialVersionUID = -6192310459083155821L;

    public ElementAlreadyReportedException(String reason) {
        super(HttpStatus.ALREADY_REPORTED, reason);
    }
}
