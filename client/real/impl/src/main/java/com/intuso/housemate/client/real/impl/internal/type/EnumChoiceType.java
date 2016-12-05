package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealListGeneratedImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Type for a multiple selection of the values of an enum
 */
public class EnumChoiceType<E extends Enum<E>> extends RealChoiceType<E> {

    private final TypeSerialiser<E> serialiser;

    /**
     * @param logger {@inheritDoc}
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param listenersFactory
     */
    @Inject
    public EnumChoiceType(@Assisted Logger logger,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          @Assisted Class enumClass,
                          ListenersFactory listenersFactory,
                          RealOptionImpl.Factory optionFactory,
                          RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(logger, id, name, description, listenersFactory, optionsFactory, convertValuesToOptions(logger, optionFactory, enumClass));
        this.serialiser = new EnumInstanceSerialiser<>(enumClass);
    }

    @Override
    public Instance serialise(E o) {
        return serialiser.serialise(o);
    }

    @Override
    public E deserialise(Instance value) {
        return serialiser.deserialise(value);
    }

    /**
     * Converts the values of an enum to option objects
     *
     * @param logger the log
     */
    private static <E extends Enum<E>> List<RealOptionImpl> convertValuesToOptions(final Logger logger,
                                                                                   final RealOptionImpl.Factory optionFactory,
                                                                                   Class<? extends Enum> enumClass) {
        return Lists.transform(Arrays.asList(enumClass.getEnumConstants()), new Function<Enum, RealOptionImpl>() {
            @Override
            public RealOptionImpl apply(Enum value) {
                return optionFactory.create(ChildUtil.logger(logger, value.name()), value.name(), value.name(), value.name(), Lists.<RealSubTypeImpl<?>>newArrayList());
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
        public Instance serialise(E value) {
            return value != null ? new Instance(value.name()) : null;
        }

        @Override
        public E deserialise(Instance value) {
            try {
                return value != null && value.getValue() != null ? Enum.valueOf(enumClass, value.getValue()) : null;
            } catch(Throwable t) {
                throw new HousemateException("Could not convert \"" + value + "\" to instance of enum " + enumClass.getName());
            }
        }
    }

    public interface Factory {
        EnumChoiceType create(Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 Class enumClass);
    }
}
