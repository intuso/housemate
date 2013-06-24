package com.intuso.housemate.api.object.type;

/**
 * Converts to and from an object to a {@link TypeInstance} representation of that object
 * @param <O> the type of the object being serialised
 */
public interface TypeSerialiser<O> {

    /**
     * Converts the object to a type instance
     * @param o the object to convert
     * @return the type instance representation of the object
     */
    public TypeInstance serialise(O o);

    /**
     * Converts a type instance to an object
     * @param instance the type instance representation of an object
     * @return the converted object
     */
    public O deserialise(TypeInstance instance);
}
