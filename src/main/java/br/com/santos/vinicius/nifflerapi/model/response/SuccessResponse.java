package br.com.santos.vinicius.nifflerapi.model.response;


import java.io.Serializable;
import java.util.List;

public class SuccessResponse extends GeneralResponse implements Serializable {

    private static final long serialVersionUID = 6252539726227602547L;
    private final List<Object> records;

    private final int recordCount;

    public SuccessResponse(List<Object> records, String message) {
        super(message);
        this.records = records;
        this.recordCount = records.size();
    }

    public List<Object> getRecords() {
        return records;
    }

    public int getRecordCount() {
        return recordCount;
    }
}
