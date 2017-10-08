package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;

/**
 * Created by tomc on 11/01/17.
 */
public class BytePrimitiveSerialiser implements TypeSerialiser<Byte> {

    public final static BytePrimitiveSerialiser INSTANCE = new BytePrimitiveSerialiser();

    @Override
    public Type.Instance serialise(Byte b) {
        return b != null ? new Type.Instance(b.toString()) : null;
    }

    @Override
    public Byte deserialise(Type.Instance value) {
        return value != null && value.getValue() != null ? Byte.parseByte(value.getValue()) : (byte) 0;
    }
}
