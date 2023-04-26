package br.com.santos.vinicius.nifflerapi.model.response;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class SuccessResponse implements Serializable {

    private static final long serialVersionUID = 6252539726227602547L;

    private final String message;

    private final List<Object> records;

    private final int recordCount;

    private final Timestamp timestamp;

    public SuccessResponse(List<Object> records, String message) {
        this.records = records;
        this.message = message;
        this.recordCount = records.size();
        this.timestamp = new Timestamp(new Date().getTime());
    }

    public List<Object> getRecords() {
        return records;
    }

    public String getMessage() {
        return message;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
