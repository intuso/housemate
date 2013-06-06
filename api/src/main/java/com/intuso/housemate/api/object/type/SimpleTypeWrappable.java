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
        String("string", "String", "Some text"),
        Integer("integer", "Integer", "A whole number"),
        Double("double", "Double", "A number"),
        Boolean("boolean", "Boolean", "True or false");

        private final String id;
        private final String name;
        private final String description;

        private Type(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private Type type;

    private SimpleTypeWrappable() {}

    public SimpleTypeWrappable(Type type) {
        super(type.getId(), type.getName(), type.getDescription());
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
