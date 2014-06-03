package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;

/**
 *
 * Data object for a simple type
 */
public final class SimpleTypeData extends TypeData<NoChildrenData> {

    private static final long serialVersionUID = -1L;

    /**
     * Enumeration of all simple types
     */
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

    public SimpleTypeData() {}

    /**
     * @param type the type of the type
     */
    public SimpleTypeData(Type type) {
        super(type.getId(), type.getName(), type.getDescription(), 1, 1);
        this.type = type;
    }

    /**
     * Get the type of the type
     * @return the type of the type
     */
    public final Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public HousemateData clone() {
        return new SimpleTypeData(type);
    }
}
