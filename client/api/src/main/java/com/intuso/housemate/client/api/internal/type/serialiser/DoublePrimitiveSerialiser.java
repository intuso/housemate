package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 11/01/17.
 */
public class DoublePrimitiveSerialiser implements TypeSerialiser<Double> {

    public final static DoublePrimitiveSerialiser INSTANCE = new DoublePrimitiveSerialiser();

    @Override
    public Type.Instance serialise(Double d) {
        return d != null ? new Type.Instance(d.toString()) : null;
    }

    @Override
    public Double deserialise(Type.Instance value) {
        return value != null && value.getValue() != null ? new Double(value.getValue()) : 0.0;
    }
}
