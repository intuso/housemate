package com.intuso.housemate.client.api.internal.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface Type<TYPE extends Type<?>> extends Object<Type.Listener<? super TYPE>> {

    /**
     *
     * Listener interface for types
     */
    interface Listener<TYPE extends Type> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of types
     */
    interface Container<TYPES extends List<? extends Type<?>, ?>> {

        /**
         * Gets the type list
         * @return the type list
         */
        TYPES getTypes();
    }

    enum Simple {

        String("string", "String", "Some text"),
        Integer("integer", "Integer", "A whole number"),
        Double("double", "Double", "A number"),
        Boolean("boolean", "Boolean", "True or false");

        private final String id;
        private final String name;
        private final String description;

        Simple(String id, String name, String description) {
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

    /**
     * Base data object for a type
     */
    abstract class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        protected Data() {}

        /**
         * @param id the type's id
         * @param name the type's name
         * @param description the type's description
         */
        public Data(String type, String id, String name, String description) {
            super(type, id, name, description);
        }
    }

    /**
     * Data object for a choice type
     */
    final class ChoiceData extends Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "type-choice";

        public ChoiceData() {}

        /**
         * @param id {@inheritDoc}
         * @param name {@inheritDoc}
         * @param description {@inheritDoc}
         */
        public ChoiceData(String id, String name, String description) {
            super(OBJECT_TYPE, id, name, description);
        }
    }

    /**
     *
     * Data object for a regex type
     */
    final class RegexData extends Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "type-regex";

        private String regexPattern;

        public RegexData() {}

        /**
         * @param id {@inheritDoc}
         * @param name {@inheritDoc}
         * @param description {@inheritDoc}
         * @param regexPattern the regex pattern that values of this type must match
         */
        public RegexData(String id, String name, String description, String regexPattern) {
            super(OBJECT_TYPE, id, name, description);
            this.regexPattern = regexPattern;
        }

        /**
         * Gets the regex pattern
         * @return the regex pattern
         */
        public String getRegexPattern() {
            return regexPattern;
        }

        public void setRegexPattern(String regexPattern) {
            this.regexPattern = regexPattern;
        }
    }

    /**
     *
     * Data object for a compound type
     */
    final class CompositeData extends Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "type-compound";

        public CompositeData() {}

        /**
         * @param id {@inheritDoc}
         * @param name {@inheritDoc}
         * @param description {@inheritDoc}
         */
        public CompositeData(String id, String name, String description) {
            super(OBJECT_TYPE, id, name, description);
        }
    }

    /**
     *
     * Data object for an object type
     */
    final class ObjectData extends Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "type-object";

        public ObjectData() {}

        /**
         * @param id {@inheritDoc}
         * @param name {@inheritDoc}
         * @param description {@inheritDoc}
         */
        public ObjectData(String id, String name, String description) {
            super(OBJECT_TYPE, id, name,  description);
        }
    }

    /**
     *
     * Data object for a simple type
     */
    final class SimpleData extends Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "type-simple";

        /**
         * Enumeration of all simple types
         */

        private Simple simple;

        public SimpleData() {}

        /**
         * @param simple the type of the type
         */
        public SimpleData(Simple simple) {
            super(OBJECT_TYPE, simple.getId(), simple.getName(), simple.getDescription());
            this.simple = simple;
        }

        /**
         * Get the type of the type
         * @return the type of the type
         */
        public final Simple getSimple() {
            return simple;
        }

        public void setSimple(Simple simple) {
            this.simple = simple;
        }
    }

    /**
     * Representation of an instance of a type
     */
    class Instance implements Serializable {

        private static final long serialVersionUID = -1L;

        private String value;
        private InstanceMap childValues;

        /**
         * Create an empty type instance
         */
        public Instance() {
            this(null);
        }

        /**
         * Create a type instance with a value
         * @param value the value
         */
        public Instance(String value) {
            this(value, new InstanceMap());
        }

        /**
         * Create a type instance with a value and predefined child values
         * @param value the value
         * @param childValues the child values
         */
        public Instance(String value, InstanceMap childValues) {
            this.value = value;
            this.childValues = (childValues != null ? childValues : new InstanceMap());
        }

        /**
         * Gets the value
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value
         * @param value the new value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the child values
         * @return the child values
         */
        public InstanceMap getChildValues() {
            return childValues;
        }

        public void setChildValues(InstanceMap childValues) {
            this.childValues = childValues;
        }

        @Override
        public boolean equals(java.lang.Object o) {
            if(o == null || !(o instanceof Instance))
                return false;
            Instance other = (Instance)o;
            if((value == null && other.value != null)
                    || !value.equals(other.value))
                return false;
            if(childValues.getChildren().size() != other.childValues.getChildren().size())
                return false;
            for(Map.Entry<String, Instances> entry : childValues.getChildren().entrySet())
                if(!other.childValues.getChildren().containsKey(entry.getKey()) || !entry.getValue().equals(other.childValues.getChildren().get(entry.getKey())))
                    return false;
            return true;
        }

        @Override
        public String toString() {
            return value + " and " + childValues.getChildren().size() + " children";
        }
    }

    /**
     * Collection of type instances, mapped by type id
     */
    class InstanceMap implements Serializable {

        private static final long serialVersionUID = -1L;

        private Map<String, Instances> children = new HashMap<>();

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public Map<String, Instances> getChildren() {
            return children;
        }

        public void setChildren(Map<String, Instances> children) {
            this.children = children == null || children instanceof Serializable ? children : new HashMap<>(children);
        }

        @Override
        public boolean equals(java.lang.Object o) {
            if(o == null || !(o instanceof InstanceMap))
                return false;
            return children.equals(((InstanceMap)o).children);
        }
    }

    /**
     * Collection of type instances
     */
    class Instances implements Serializable {

        private static final long serialVersionUID = -1L;

        private java.util.List<Instance> elements = new ArrayList<>();

        public Instances() {}

        public Instances(Instance... elements) {
            this(Arrays.asList(elements));
        }

        public Instances(java.util.List<Instance> elements) {
            this.elements = elements;
        }

        public java.util.List<Instance> getElements() {
            return elements;
        }

        public void setElements(java.util.List<Instance> elements) {
            this.elements = elements == null || elements instanceof Serializable ? elements : new ArrayList<>(elements);
        }

        public String getFirstValue() {
            return elements.size() > 0 && elements.get(0) != null ? elements.get(0).getValue() : null;
        }

        @Override
        public boolean equals(java.lang.Object o) {
            if(o == null || !(o instanceof Instances))
                return false;
            return elements.equals(((Instances) o).elements);
        }
    }
}
