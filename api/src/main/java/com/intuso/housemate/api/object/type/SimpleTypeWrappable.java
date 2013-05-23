package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public final class SimpleTypeWrappable extends TypeWrappable<NoChildrenWrappable> {

    public enum Type {
        String("Some text"),
        Integer("A whole number"),
        Double("A number"),
        Boolean("True or false");

        private final String description;

        private Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private Type type;

    private SimpleTypeWrappable() {}

    public SimpleTypeWrappable(Type type) {
        super(type.name().toLowerCase(), type.name(), type.getDescription());
        this.type = type;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new SimpleTypeWrappable(type);
    }

    public final Type getType() {
        return type;
    }
}
