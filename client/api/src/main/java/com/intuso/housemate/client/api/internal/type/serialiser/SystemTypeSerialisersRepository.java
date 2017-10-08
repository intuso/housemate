package com.intuso.housemate.client.api.internal.type.serialiser;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.TypeSpec;

import java.util.Map;

/**
 * Created by tomc on 13/05/16.
 */
public final class SystemTypeSerialisersRepository implements TypeSerialiser.Repository {

    private final Map<TypeSpec, TypeSerialiser<?>> serialisers = Maps.newHashMap();

    @Inject
    public SystemTypeSerialisersRepository(// primitive types
                                           BooleanObjectSerialiser booleanObjectSerialiser,
                                           ByteObjectSerialiser byteObjectSerialiser,
                                           DoubleObjectSerialiser doubleObjectSerialiser,
                                           IntegerObjectSerialiser integerObjectSerialiser,
                                           StringSerialiser stringSerialiser) {
        serialisers.put(new TypeSpec(Boolean.class), booleanObjectSerialiser);
        serialisers.put(new TypeSpec(boolean.class), booleanObjectSerialiser);
        serialisers.put(new TypeSpec(Byte.class), byteObjectSerialiser);
        serialisers.put(new TypeSpec(byte.class), byteObjectSerialiser);
        serialisers.put(new TypeSpec(Double.class), doubleObjectSerialiser);
        serialisers.put(new TypeSpec(double.class), doubleObjectSerialiser);
        serialisers.put(new TypeSpec(Integer.class), integerObjectSerialiser);
        serialisers.put(new TypeSpec(int.class), integerObjectSerialiser);
        serialisers.put(new TypeSpec(String.class), stringSerialiser);
        serialisers.put(new TypeSpec(String.class, "email"), stringSerialiser);
    }

    @Override
    public <O> TypeSerialiser<O> getSerialiser(TypeSpec typeSpec) {
        return (TypeSerialiser<O>) serialisers.get(typeSpec);
    }
}
