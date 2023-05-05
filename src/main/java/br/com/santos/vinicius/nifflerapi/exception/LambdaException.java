package br.com.santos.vinicius.nifflerapi.exception;

public class LambdaException extends RuntimeException {

    private static final long serialVersionUID = -5342023394585824780L;

    public LambdaException() {
    }

    public LambdaException(String message) {
        super(message);
    }

    public LambdaException(String message, Throwable cause) {
        super(message, cause);
    }

    public LambdaException(Throwable cause) {
        super(cause);
    }
}
