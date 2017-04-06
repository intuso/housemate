package com.intuso.housemate.webserver.database.model;

import java.util.List;

/**
 * Created by tomc on 21/01/17.
 */
public class Page<T> {

    private long offset;
    private long total;
    private List<T> elements;

    public Page() {}

    public Page(long offset, long total, List<T> elements) {
        this.offset = offset;
        this.total = total;
        this.elements = elements;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }
}
