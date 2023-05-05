package br.com.santos.vinicius.nifflerapi.controller.handler;

import br.com.santos.vinicius.nifflerapi.exception.ElementAlreadyReportedException;
import br.com.santos.vinicius.nifflerapi.exception.InternalServerException;
import br.com.santos.vinicius.nifflerapi.exception.NoSuchElementFoundException;
import br.com.santos.vinicius.nifflerapi.model.response.ErrorResponse;
import br.com.santos.vinicius.nifflerapi.model.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExceptionsHandler {


    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response> handleNoSuchElementFound(NoSuchElementFoundException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(ElementAlreadyReportedException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<Response> handleElementAlreadyReported(ElementAlreadyReportedException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleInternalServerError(InternalServerException exception) {
        return handleException(exception);
    }

    private ResponseEntity<Response> handleException(ResponseStatusException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getReason(),
                exception.getStatus().value(), exception.getStatus().name());

        return ResponseEntity.status(exception.getStatus()).body(new Response(errorResponse));
    }

}
