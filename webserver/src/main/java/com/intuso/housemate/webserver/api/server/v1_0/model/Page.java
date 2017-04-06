package com.intuso.housemate.webserver.api.server.v1_0.model;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static <F, T> Page<T> from(com.intuso.housemate.webserver.database.model.Page<F> page, Function<F, T> mapper) {
        return page == null ? null : new Page<>(page.getOffset(), page.getTotal(), page.getElements().stream().map(mapper).collect(Collectors.toList()));
    }
}
