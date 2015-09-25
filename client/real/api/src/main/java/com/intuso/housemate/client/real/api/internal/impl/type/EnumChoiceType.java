package com.intuso.housemate.client.real.api.internal.impl.type;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.intuso.housemate.client.real.api.internal.RealOption;
import com.intuso.housemate.client.real.api.internal.RealSubType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * Type for a multiple selection of the values of an enum
 */
public abstract class EnumChoiceType<E extends Enum<E>> extends RealChoiceType<E> {

    private final TypeSerialiser<E> serialiser;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param enumClass the class of the enum
     * @param values the values of the enum
     */
    protected EnumChoiceType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, Class<E> enumClass, E... values) {
        this(log, listenersFactory, id, name, description, minValues,
                maxValues,
                new EnumMap<E, List<RealSubType<?>>>(enumClass), new EnumInstanceSerialiser<>(enumClass), values);
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param enumClass the class of the enum
     * @param optionSubTypes the subtypes for each enum value
     * @param values the values of the enum
     */
    protected EnumChoiceType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, Class<E> enumClass,
                             EnumMap<E, List<RealSubType<?>>> optionSubTypes, E... values) {
        this(log, listenersFactory, id, name, description, minValues, maxValues,
                optionSubTypes, new EnumInstanceSerialiser<>(enumClass), values);
    }
    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param enumClass the class of the enum
     * @param elementSerialiser the serialiser for the enum elements
     * @param values the values of the enum
     */
    protected EnumChoiceType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, Class<E> enumClass, TypeSerialiser<E> elementSerialiser, E... values) {
        this(log, listenersFactory, id, name, description, minValues,
                maxValues, new EnumMap<E, List<RealSubType<?>>>(enumClass), elementSerialiser, values);
    }
    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param optionSubTypes the subtypes for each enum value
     * @param elementSerialiser the serialiser for the enum elements
     * @param values the values of the enum
     */
    protected EnumChoiceType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, EnumMap<E, List<RealSubType<?>>> optionSubTypes,
                             TypeSerialiser<E> elementSerialiser, E... values) {
        super(log, listenersFactory, id, name, description, minValues,
                maxValues, convertValuesToOptions(log, listenersFactory, values, optionSubTypes));
        this.serialiser = elementSerialiser;
    }

    @Override
    public TypeInstance serialise(E o) {
        return serialiser.serialise(o);
    }

    @Override
    public E deserialise(TypeInstance value) {
        return serialiser.deserialise(value);
    }

    /**
     * Converts the values of an enum to option objects
     *
     * @param log the log
     * @param listenersFactory
     *@param values the enum's values
     * @param optionSubTypes the subtypes for each enum value   @return a list of option objects, one for each value of the enum
     */
    private static <E extends Enum<E>> List<RealOption> convertValuesToOptions(final Log log, final ListenersFactory listenersFactory, E[] values,
                                                                               final EnumMap<E, List<RealSubType<?>>> optionSubTypes) {
        return Lists.transform(Arrays.asList(values), new Function<E, RealOption>() {
            @Override
            public RealOption apply(E value) {
                if(optionSubTypes.containsKey(value))
                    return new RealOption(log, listenersFactory, value.name(), value.name(), value.name(), optionSubTypes.get(value));
                else
                    return new RealOption(log, listenersFactory, value.name(), value.name(), value.name());
            }
        });
    }

    /**
     * Serialiser for enum values
     * @param <E> the enum type
     */
    public static class EnumInstanceSerialiser<E extends Enum<E>> implements TypeSerialiser<E> {

        private final Class<E> enumClass;

        /**
         * @param enumClass the class of the enum
         */
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
                throw new HousemateCommsException("Could not convert \"" + value + "\" to instance of enum " + enumClass.getName());
            }
        }
    }
}
