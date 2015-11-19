package com.intuso.housemate.comms.api.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomc on 06/10/15.
 */
public class ChildOverviews implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private List<ChildOverview> childOverviews;
    private String error;

    public ChildOverviews() {
    }

    public ChildOverviews(List<ChildOverview> childOverviews) {
        this(childOverviews, null);
    }

    public ChildOverviews(String error) {
        this(null, error);
    }

    public ChildOverviews(List<ChildOverview> childOverviews, String error) {
        this.childOverviews = childOverviews;
        this.error = error;
    }

    public List<ChildOverview> getChildOverviews() {
        return childOverviews;
    }

    public void setChildOverviews(List<ChildOverview> childOverviews) {
        this.childOverviews = childOverviews == null || childOverviews instanceof Serializable ? childOverviews : new ArrayList<>(childOverviews);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public void ensureSerialisable() {
        if (childOverviews != null && !(childOverviews instanceof ArrayList))
            childOverviews = new ArrayList<>(childOverviews);
    }
}
