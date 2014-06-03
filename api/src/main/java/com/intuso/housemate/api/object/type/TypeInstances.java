package com.intuso.housemate.api.object.type;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.comms.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Collection of type instances
 */
public class TypeInstances implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private List<TypeInstance> elements = Lists.newArrayList();

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
        this.elements = elements == null || elements instanceof Serializable ? elements : Lists.newArrayList(elements);
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

    @Override
    public String toString() {
        return Joiner.on(",").join(Lists.transform(elements, new Function<TypeInstance, String>() {
            @Override
            public String apply(TypeInstance typeInstance) {
                return typeInstance != null && typeInstance.getValue() != null ? typeInstance.getValue() : "null";
            }
        }));
    }

    @Override
    public void ensureSerialisable() {
        if(elements != null && !(elements instanceof ArrayList))
            elements = Lists.newArrayList(elements);
        if(elements != null)
            for(TypeInstance element : elements)
                if(element != null)
                    element.ensureSerialisable();
    }
}
