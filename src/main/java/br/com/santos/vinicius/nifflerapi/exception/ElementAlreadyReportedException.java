package br.com.santos.vinicius.nifflerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ElementAlreadyReportedException extends ResponseStatusException {


    private static final long serialVersionUID = -6192310459083155821L;

    public ElementAlreadyReportedException(HttpStatus status) {
        super(status);
    }

    public ElementAlreadyReportedException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ElementAlreadyReportedException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public ElementAlreadyReportedException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
