package br.com.santos.vinicius.nifflerapi.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 8918567723729402172L;


    private String message;

    private int status;

    private String error;

    @JsonProperty(value = "error_code")
    private Long errorCode;

    private Timestamp timestamp;

    public ErrorResponse(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.errorCode = 4041L;
        this.timestamp = new Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public String getMessage() {
        return message;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
