package br.com.santos.vinicius.nifflerapi.exception;

public class LambdaException extends RuntimeException {

    private static final long serialVersionUID = -5342023394585824780L;

    public LambdaException(String message, Throwable cause) {
        super(message, cause);
    }
}
