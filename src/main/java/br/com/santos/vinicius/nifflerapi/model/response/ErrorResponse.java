package br.com.santos.vinicius.nifflerapi.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ErrorResponse extends GeneralResponse implements Serializable {

    private static final long serialVersionUID = 8918567723729402172L;


    private int status;

    private String error;

    @JsonProperty(value = "error_code")
    private Long errorCode;

    public ErrorResponse(String message, int status, String error) {
        super(message);
        this.status = status;
        this.error = error;
        this.errorCode = 4041L;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Long getErrorCode() {
        return errorCode;
    }
}
