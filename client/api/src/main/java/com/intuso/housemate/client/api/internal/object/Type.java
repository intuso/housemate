package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.TypeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface Type<TYPE extends Type<?>> extends Object<Type.Data, Type.Listener<? super TYPE>, TypeView> {

    /**
     *
     * Listener interface for types
     */
    interface Listener<TYPE extends Type> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of types
     */
    interface Container<TYPES extends Iterable<? extends Type<?>>> {

        /**
         * Gets the type list
         * @return the type list
         */
        TYPES getTypes();
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

        public final static String OBJECT_CLASS = "type-choice";

        public ChoiceData() {}

        /**
         * @param id {@inheritDoc}
         * @param name {@inheritDoc}
         * @param description {@inheritDoc}
         */
        public ChoiceData(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }

    /**
     *
     * Data object for a regex type
     */
    final class RegexData extends Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "type-regex";

        private String regexPattern;

        public RegexData() {}

        /**
         * @param id {@inheritDoc}
         * @param name {@inheritDoc}
         * @param description {@inheritDoc}
         * @param regexPattern the regex pattern that values of this type must match
         */
        public RegexData(String id, String name, String description, String regexPattern) {
            super(OBJECT_CLASS, id, name, description);
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

        public final static String OBJECT_CLASS = "type-compound";

        public CompositeData() {}

        /**
         * @param id {@inheritDoc}
         * @param name {@inheritDoc}
         * @param description {@inheritDoc}
         */
        public CompositeData(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }

    /**
     *
     * Data object for an object type
     */
    final class ObjectData extends Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "type-object";

        public ObjectData() {}

        /**
         * @param id {@inheritDoc}
         * @param name {@inheritDoc}
         * @param description {@inheritDoc}
         */
        public ObjectData(String id, String name, String description) {
            super(OBJECT_CLASS, id, name,  description);
        }
    }

    /**
     *
     * Data object for a simple type
     */
    final class PrimitiveData extends Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "type-primitive";

        public PrimitiveData() {}

        public PrimitiveData(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
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

        public void append(String indent, StringBuilder stringBuilder) {
            stringBuilder.append("{\n").append(indent).append("\"value\": ");
            if(value != null)
                stringBuilder.append("\"").append(value).append("\"");
            else
                stringBuilder.append("null");
            stringBuilder.append("\n").append(indent).append("\"children\": ");
            if(childValues != null)
                childValues.append(indent + "\t", stringBuilder);
            else
                stringBuilder.append("{}");
            stringBuilder.append("\n").append(indent).append("}");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            append("", sb);
            return sb.toString();
        }

        @Override
        public boolean equals(java.lang.Object o) {
            if(o == null || !(o instanceof Instance))
                return false;
            Instance other = (Instance)o;
            if((value == null && other.value != null)
                    || !value.equals(other.value))
                return false;
            if(childValues.size() != other.childValues.size())
                return false;
            for(Map.Entry<String, Instances> entry : childValues.entrySet())
                if(!other.childValues.containsKey(entry.getKey()) || !entry.getValue().equals(other.childValues.get(entry.getKey())))
                    return false;
            return true;
        }
    }

    /**
     * Collection of type instances, mapped by type id
     */
    class InstanceMap extends HashMap<String, Instances> implements Serializable {

        private static final long serialVersionUID = -1L;

        public void append(String indent, StringBuilder stringBuilder) {
            stringBuilder.append("{");
            if(size() > 0) {
                stringBuilder.append("\n").append(indent).append("\t");
                for (Map.Entry<String, Instances> entry : entrySet()) {
                    stringBuilder.append("\"").append(entry.getKey()).append("\": ");
                    entry.getValue().append(indent + "\t", stringBuilder);
                    stringBuilder.append("\n").append(indent).append("\t");
                }
            }
            stringBuilder.append("}");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            append("", sb);
            return sb.toString();
        }
    }

    /**
     * Collection of type instances
     */
    class Instances extends ArrayList<Instance> implements Serializable {

        private static final long serialVersionUID = -1L;

        public Instances() {}

        public Instances(Instance... elements) {
            this(Arrays.asList(elements));
        }

        public Instances(java.util.List<Instance> elements) {
            this.addAll(elements);
        }

        public String getFirstValue() {
            return size() > 0 && get(0) != null ? get(0).getValue() : null;
        }

        public void append(String indent, StringBuilder stringBuilder) {
            stringBuilder.append("[");
            if(size() > 0) {
                for (Instance element : this) {
                    if (element == null)
                        stringBuilder.append("null");
                    else
                        element.append(indent, stringBuilder);
                }
            }
            stringBuilder.append("]");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            append("", sb);
            return sb.toString();
        }
    }
}
