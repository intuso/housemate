package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 11/01/17.
 */
public class StringSerialiser implements TypeSerialiser<String> {

    public final static StringSerialiser INSTANCE = new StringSerialiser();

    @Override
    public Type.Instance serialise(String s) {
        return s != null ? new Type.Instance(s) : null;
    }

    @Override
    public String deserialise(Type.Instance value) {
        return value != null ? value.getValue() : null;
    }
}
