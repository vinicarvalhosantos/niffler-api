package br.com.santos.vinicius.nifflerapi.model.response;

import java.io.Serializable;

public class Response implements Serializable {

    private static final long serialVersionUID = 4938986472098639739L;

    private final Object data;

    public Response(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
