package com.intuso.housemate.client.api.internal.type;

import java.lang.reflect.Type;

/**
 * Created by tomc on 04/01/17.
 */
public final class TypeSpec {

    private final Type type;
    private final String restriction;

    public TypeSpec(Type type) {
        this(type, null);
    }

    public TypeSpec(Type type, String restriction) {
        this.type = type;
        this.restriction = restriction == null || "".equals(restriction) ? null : restriction;
    }

    public Type getType() {
        return type;
    }

    public String getRestriction() {
        return restriction;
    }

    @Override
    public String toString() {
        return (type != null ? type.toString() : "") +
                (restriction != null ? "." + restriction : "");
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TypeSpec && toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
