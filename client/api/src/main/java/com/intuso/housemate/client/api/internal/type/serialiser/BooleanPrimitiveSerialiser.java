package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 11/01/17.
 */
public class BooleanPrimitiveSerialiser implements TypeSerialiser<Boolean> {

    public final static BooleanPrimitiveSerialiser INSTANCE = new BooleanPrimitiveSerialiser();

    @Override
    public Type.Instance serialise(Boolean b) {
        return b != null ? new Type.Instance(b.toString()) : null;
    }

    @Override
    public Boolean deserialise(Type.Instance value) {
        return value != null && value.getValue() != null ? Boolean.parseBoolean(value.getValue()) : true;
    }
}
