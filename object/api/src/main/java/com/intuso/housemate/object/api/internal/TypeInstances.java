package com.intuso.housemate.object.api.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Collection of type instances
 */
public class TypeInstances {

    private static final long serialVersionUID = -1L;

    private List<TypeInstance> elements = new ArrayList<>();

    public TypeInstances() {}

    public TypeInstances(TypeInstance ... elements) {
        this(Arrays.asList(elements));
    }

    public TypeInstances(List<TypeInstance> elements) {
        this.elements = elements;
    }

    public List<TypeInstance> getElements() {
        return elements;
    }

    public void setElements(List<TypeInstance> elements) {
        this.elements = elements == null || elements instanceof Serializable ? elements : new ArrayList<>(elements);
    }

    public String getFirstValue() {
        return elements.size() > 0 && elements.get(0) != null ? elements.get(0).getValue() : null;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstances))
            return false;
        return elements.equals(((TypeInstances) o).elements);
    }
}
