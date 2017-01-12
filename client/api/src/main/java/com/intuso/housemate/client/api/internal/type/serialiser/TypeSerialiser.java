package com.intuso.housemate.client.api.internal.type.serialiser;

import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;

/**
 * Converts to and from an object to a {@link Type.Instance} representation of that object
 * @param <DATA_TYPE> the type of the object being serialised
 */
public interface TypeSerialiser<DATA_TYPE> {

    /**
     * Converts the object to a type instance
     * @param dataType the object to convert
     * @return the type instance representation of the object
     */
    Type.Instance serialise(DATA_TYPE dataType);

    /**
     * Converts a type instance to an object
     * @param instance the type instance representation of an object
     * @return the converted object
     */
    DATA_TYPE deserialise(Type.Instance instance);

    interface Repository {
        <O> TypeSerialiser<O> getSerialiser(TypeSpec typeSpec);
    }
}
