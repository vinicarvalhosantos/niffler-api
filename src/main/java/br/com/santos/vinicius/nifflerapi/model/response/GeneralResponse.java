package br.com.santos.vinicius.nifflerapi.model.response;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class GeneralResponse implements Serializable {

    private static final long serialVersionUID = -3318580974564343629L;
    private final String message;

    private final Timestamp timestamp;

    public GeneralResponse(String message) {
        this.message = message;
        this.timestamp = new Timestamp(new Date().getTime());
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
