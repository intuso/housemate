package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 11/01/17.
 */
public class DoubleObjectSerialiser implements TypeSerialiser<Double> {

    public final static DoubleObjectSerialiser INSTANCE = new DoubleObjectSerialiser();

    @Override
    public Type.Instance serialise(Double d) {
        return d != null ? new Type.Instance(d.toString()) : null;
    }

    @Override
    public Double deserialise(Type.Instance value) {
        return value != null && value.getValue() != null ? new Double(value.getValue()) : null;
    }
}
