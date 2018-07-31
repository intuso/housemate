package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.type.serialiser.StringSerialiser;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for text input restricted to text that matches a regex
 */
public class RealRegexType extends RealTypeImpl<String> {

    private final StringSerialiser stringSerialiser;

    /**
     * @param logger the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param managedCollectionFactory
     * @param regexPattern the regex pattern that values must match
     */
    @Inject
    protected RealRegexType(@Assisted Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted("regexPattern") String regexPattern,
                            ManagedCollectionFactory managedCollectionFactory) {
        super(logger, new RegexData(id, name, description, regexPattern), managedCollectionFactory);
        stringSerialiser = new StringSerialiser();
    }

    @Override
    public Instance serialise(String value) {
        return stringSerialiser.serialise(value);
    }

    @Override
    public String deserialise(Instance instance) {
        return stringSerialiser.deserialise(instance);
    }

    public interface Factory {
        RealRegexType create(Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted("regexPattern") String regexPattern);
    }
}
