package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.type.serialiser.EnumSerialiser;
import com.intuso.housemate.client.api.internal.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealListGeneratedImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.utilities.listener.ManagedCollectionFactory;
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
     * @param managedCollectionFactory
     */
    @Inject
    public EnumChoiceType(@Assisted Logger logger,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          @Assisted Class enumClass,
                          ManagedCollectionFactory managedCollectionFactory,
                          RealOptionImpl.Factory optionFactory,
                          RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(logger, id, name, description, convertValuesToOptions(logger, optionFactory, enumClass), managedCollectionFactory, optionsFactory);
        this.serialiser = new EnumSerialiser<>(enumClass);
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
        return Lists.transform(Arrays.<Enum>asList(enumClass.getEnumConstants()), new Function<Enum, RealOptionImpl>() {
            @Override
            public RealOptionImpl apply(Enum value) {
                return optionFactory.create(ChildUtil.logger(logger, value.name()), value.name(), value.name(), value.name(), Lists.<RealSubTypeImpl<?>>newArrayList());
            }
        });
    }

    public interface Factory {
        EnumChoiceType create(Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 Class enumClass);
    }
}
