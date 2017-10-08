package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 11/01/17.
 */
public class IntegerPrimitiveSerialiser implements TypeSerialiser<Integer> {

    public final static IntegerPrimitiveSerialiser INSTANCE = new IntegerPrimitiveSerialiser();

    @Override
    public Type.Instance serialise(Integer i) {
        return i != null ? new Type.Instance(i.toString()) : null;
    }

    @Override
    public Integer deserialise(Type.Instance value) {
        return value != null && value.getValue() != null ? new Integer(value.getValue()) : 0;
    }
}
