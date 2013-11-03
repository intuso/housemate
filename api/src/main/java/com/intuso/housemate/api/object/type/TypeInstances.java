package com.intuso.housemate.api.object.type;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.comms.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Collection of type instances
 */
public class TypeInstances extends ArrayList<TypeInstance> implements Message.Payload {

    private static final long serialVersionUID = -1L;

    public TypeInstances() {}

    public TypeInstances(TypeInstance ... instances) {
        this(Arrays.asList(instances));
    }

    public TypeInstances(Collection<? extends TypeInstance> instances) {
        super(instances);
    }

    public String getFirstValue() {
        return size() > 0 && get(0) != null ? get(0).getValue() : null;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstances))
            return false;
        TypeInstances other = (TypeInstances)o;
        if(size() != other.size())
            return false;
        for(int i = 0; i < size(); i++)
            if(!get(i).equals(other.get(i)))
                return false;
        return true;
    }

    @Override
    public String toString() {
        return Joiner.on(",").join(Lists.transform(this, new Function<TypeInstance, String>() {
            @Override
            public String apply(TypeInstance typeInstance) {
                return typeInstance != null && typeInstance.getValue() != null ? typeInstance.getValue() : "null";
            }
        }));
    }
}
