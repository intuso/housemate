package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 11/01/17.
 */
public class BooleanObjectSerialiser implements TypeSerialiser<Boolean> {

    public final static BooleanObjectSerialiser INSTANCE = new BooleanObjectSerialiser();

    @Override
    public Type.Instance serialise(Boolean b) {
        return b != null ? new Type.Instance(b.toString()) : null;
    }

    @Override
    public Boolean deserialise(Type.Instance value) {
        return value != null && value.getValue() != null ? Boolean.parseBoolean(value.getValue()) : null;
    }
}
