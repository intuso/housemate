package com.intuso.housemate.object.api.internal;

/**
 * Converts to and from an object to a {@link TypeInstance} representation of that object
 * @param <DATA_TYPE> the type of the object being serialised
 */
public interface TypeSerialiser<DATA_TYPE> {

    /**
     * Converts the object to a type instance
     * @param dataType the object to convert
     * @return the type instance representation of the object
     */
    TypeInstance serialise(DATA_TYPE dataType);

    /**
     * Converts a type instance to an object
     * @param instance the type instance representation of an object
     * @return the converted object
     */
    DATA_TYPE deserialise(TypeInstance instance);
}
