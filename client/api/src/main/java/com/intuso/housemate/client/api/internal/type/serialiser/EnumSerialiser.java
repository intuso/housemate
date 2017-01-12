package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 12/01/17.
 */
public class EnumSerialiser<E extends Enum<E>> implements TypeSerialiser<E> {

    private final Class<E> enumClass;

    /**
     * @param enumClass the class of the enum
     */
    public EnumSerialiser(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Type.Instance serialise(E value) {
        return value != null ? new Type.Instance(value.name()) : null;
    }

    @Override
    public E deserialise(Type.Instance value) {
        try {
            return value != null && value.getValue() != null ? Enum.valueOf(enumClass, value.getValue()) : null;
        } catch(Throwable t) {
            throw new HousemateException("Could not convert \"" + value + "\" to instance of enum " + enumClass.getName());
        }
    }
}
