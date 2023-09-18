package br.com.santos.vinicius.nifflerapi.model.response;


import br.com.santos.vinicius.nifflerapi.filter.ExcludeZerosFilter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

public class SuccessResponse extends GeneralResponse implements Serializable {

    private static final long serialVersionUID = 6252539726227602547L;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ExcludeZerosFilter.class)
    private int page;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ExcludeZerosFilter.class)
    private int limit;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ExcludeZerosFilter.class)
    private int totalPages;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Long totalElements;

    private final List<Object> records;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ExcludeZerosFilter.class)
    private final int recordCount;

    public SuccessResponse(List<Object> records, String message) {
        super(message);
        this.records = records;
        this.recordCount = records.size();
    }

    public SuccessResponse(List<Object> records, String message, int page, int totalPages, Long totalElements, int limit) {
        super(message);
        this.records = records;
        this.recordCount = records.size();
        this.page = page;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.limit = limit;
    }

    public List<Object> getRecords() {
        return records;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public int getLimit() {
        return limit;
    }
}
