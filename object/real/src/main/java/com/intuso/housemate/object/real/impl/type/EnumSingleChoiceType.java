package com.intuso.housemate.object.real.impl.type;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class EnumSingleChoiceType<E extends Enum<E>> extends RealSingleChoiceType<E> {

    private final Class<E> enumClass;
    private final TypeSerialiser<E> serialiser;

    protected EnumSingleChoiceType(RealResources resources, String id, String name, String description,
                                   Class<E> enumClass, E[] values) {
        this(resources, id, name, description, enumClass, values, new EnumMap<E, List<RealType<?, ?, ?>>>(enumClass),
                new EnumInstanceSerialiser<E>(enumClass));
    }

    protected EnumSingleChoiceType(RealResources resources, String id, String name, String description,
                                   Class<E> enumClass, E[] values, TypeSerialiser<E> serialiser) {
        this(resources, id, name, description, enumClass, values, new EnumMap<E, List<RealType<?, ?, ?>>>(enumClass),
                serialiser);
    }

    protected EnumSingleChoiceType(RealResources resources, String id, String name, String description,
                                   Class<E> enumClass, E[] values, EnumMap<E, List<RealType<?, ?, ?>>> optionSubTypes) {
        this(resources, id, name, description, enumClass, values, optionSubTypes, new EnumInstanceSerialiser<E>(enumClass));
    }

    protected EnumSingleChoiceType(RealResources resources, String id, String name, String description,
                                   Class<E> enumClass, E[] values, EnumMap<E, List<RealType<?, ?, ?>>> optionSubTypes,
                                   TypeSerialiser<E> serialiser) {
        super(resources, id, name, description, convertValuesToOptions(resources, values, optionSubTypes));
        this.enumClass = enumClass;
        this.serialiser = serialiser;
    }

    @Override
    public TypeInstance serialise(E o) {
        return serialiser.serialise(o);
    }

    @Override
    public E deserialise(TypeInstance value) {
        return serialiser.deserialise(value);
    }

    public static <E extends Enum<E>> List<RealOption> convertValuesToOptions(final RealResources resources, E[] values,
                                                          final EnumMap<E, List<RealType<?, ?, ?>>> optionSubTypes) {
        return Lists.transform(Arrays.asList(values), new Function<E, RealOption>() {
            @Override
            public RealOption apply(@Nullable E value) {
                if(optionSubTypes.containsKey(value))
                    return new RealOption(resources, value.name(), value.name(), value.name(), optionSubTypes.get(value));
                else
                    return new RealOption(resources, value.name(), value.name(), value.name());
            }
        });
    }

    public static class EnumInstanceSerialiser<E extends Enum<E>> implements TypeSerialiser<E> {

        private final Class<E> enumClass;

        public EnumInstanceSerialiser(Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public TypeInstance serialise(E value) {
            return value != null ? new TypeInstance(value.name()) : null;
        }

        @Override
        public E deserialise(TypeInstance value) {
            try {
                return value != null && value.getValue() != null ? Enum.valueOf(enumClass, value.getValue()) : null;
            } catch(Throwable t) {
                throw new HousemateRuntimeException("Could not convert \"" + value + "\" to instance of enum " + enumClass.getName());
            }
        }
    }
}
