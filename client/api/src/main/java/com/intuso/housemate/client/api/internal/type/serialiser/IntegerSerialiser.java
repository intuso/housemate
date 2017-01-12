package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 11/01/17.
 */
public class IntegerSerialiser implements TypeSerialiser<Integer> {

    public final static IntegerSerialiser INSTANCE = new IntegerSerialiser();

    @Override
    public Type.Instance serialise(Integer i) {
        return i != null ? new Type.Instance(i.toString()) : null;
    }

    @Override
    public Integer deserialise(Type.Instance value) {
        return value != null && value.getValue() != null ? new Integer(value.getValue()) : null;
    }
}
