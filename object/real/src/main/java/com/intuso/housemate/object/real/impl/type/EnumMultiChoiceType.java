package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

/**
 */
public abstract class EnumMultiChoiceType<E extends Enum<E>> extends RealMultiChoiceType<E> {

    private final TypeSerialiser<Set<E>> serialiser;

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description,
                                  Class<E> enumClass, E[] values) {
        this(resources, id, name, description, enumClass, values, new EnumMap<E, List<RealSubType<?>>>(enumClass),
                new EnumSingleChoiceType.EnumInstanceSerialiser<E>(enumClass));
    }

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description,
                                  Class<E> enumClass, E[] values, EnumMap<E, List<RealSubType<?>>> optionSubTypes) {
        this(resources, id, name, description, enumClass, values, optionSubTypes,
                new EnumSingleChoiceType.EnumInstanceSerialiser<E>(enumClass));
    }

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description,
                                  Class<E> enumClass, E[] values, TypeSerialiser<E> elementSerialiser) {
        this(resources, id, name, description, enumClass, values, new EnumMap<E, List<RealSubType<?>>>(enumClass),
                elementSerialiser);
    }

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description,
                                  Class<E> enumClass, E[] values, EnumMap<E, List<RealSubType<?>>> optionSubTypes,
                                  TypeSerialiser<E> elementSerialiser) {
        super(resources, id, name, description,
                EnumSingleChoiceType.convertValuesToOptions(resources, values, optionSubTypes));
        this.serialiser = new MultiChoiceSerialiser<E>(elementSerialiser);
    }

    @Override
    public TypeInstance serialise(Set<E> o) {
        return serialiser.serialise(o);
    }

    @Override
    public Set<E> deserialise(TypeInstance value) {
        return serialiser.deserialise(value);
    }
}
