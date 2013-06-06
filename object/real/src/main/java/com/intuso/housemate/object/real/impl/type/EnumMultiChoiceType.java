package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class EnumMultiChoiceType<E extends Enum<E>> extends RealMultiChoiceType<E> {

    private final TypeSerialiser<Set<E>> serialiser;

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description,
                                  Class<E> enumClass, E[] values) {
        this(resources, id, name, description, enumClass, values, new EnumMap<E, List<RealType<?, ?, ?>>>(enumClass),
                new EnumSingleChoiceType.EnumInstanceSerialiser<E>(enumClass));
    }

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description,
                                  Class<E> enumClass, E[] values, EnumMap<E, List<RealType<?, ?, ?>>> optionSubTypes) {
        this(resources, id, name, description, enumClass, values, optionSubTypes,
                new EnumSingleChoiceType.EnumInstanceSerialiser<E>(enumClass));
    }

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description,
                                  Class<E> enumClass, E[] values, TypeSerialiser<E> elementSerialiser) {
        this(resources, id, name, description, enumClass, values, new EnumMap<E, List<RealType<?, ?, ?>>>(enumClass),
                elementSerialiser);
    }

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description,
                                  Class<E> enumClass, E[] values, EnumMap<E, List<RealType<?, ?, ?>>> optionSubTypes,
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
