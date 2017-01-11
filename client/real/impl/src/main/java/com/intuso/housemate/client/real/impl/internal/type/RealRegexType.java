package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for text input restricted to text that matches a regex
 */
public class RealRegexType extends RealTypeImpl<String> {

    /**
     * @param logger the log
     * @param extension the type's extension
     * @param name the type's name
     * @param description the type's description
     * @param listenersFactory
     * @param regexPattern the regex pattern that values must match
     */
    @Inject
    protected RealRegexType(@Assisted Logger logger,
                            @Assisted("extension") String extension,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted("regexPattern") String regexPattern,
                            ListenersFactory listenersFactory) {
        super(logger,
                new RegexData(String.class.getName() + "." + extension, name, description, regexPattern),
                new TypeSpec(String.class, extension),
                listenersFactory);
    }

    @Override
    public Instance serialise(String value) {
        return value == null ? null : new Instance(value);
    }

    @Override
    public String deserialise(Instance instance) {
        return instance == null || instance.getValue() == null ? null : instance.getValue();
    }

    public interface Factory {
        RealRegexType create(Logger logger,
                             @Assisted("extension") String extension,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted("regexPattern") String regexPattern);
    }
}
